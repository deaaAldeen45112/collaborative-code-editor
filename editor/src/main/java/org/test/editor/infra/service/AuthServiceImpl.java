package org.test.editor.infra.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.test.editor.core.service.AuthService;
import org.test.editor.core.service.UserService;
import org.test.editor.presentation.config.GithubPropertiesConfig;
import org.test.editor.core.dto.*;
import org.test.editor.core.exception.DuplicateResourceException;
import org.test.editor.core.exception.InvalidTokenException;
import org.test.editor.core.mapper.UserMapper;
import org.test.editor.core.model.User;
import org.test.editor.util.CustomUserDetails;
import org.test.editor.util.constant.UserRole;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.test.editor.util.PasswordGenerator.generateRandomPassword;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final GithubPropertiesConfig githubPropertiesConfig;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Value("${google.client-id}")
    private String googleClientId;



    public LoginResDTO login(LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return createLoginResponse(userDetails);
    }

    private LoginResDTO createLoginResponse(UserDetails userDetails) {
        User user =((CustomUserDetails)userDetails).getUser();
        String token = jwtService.generateToken(userDetails);
        UserDTO userDTO = new UserDTO(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getRoleId()
        );
        return new LoginResDTO(userDTO, token);
    }
    public LoginResDTO googleLogin(GmailLoginDTO gmailLoginDto) throws InvalidTokenException, GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        GoogleIdToken idToken = verifier.verify(gmailLoginDto.getIdToken());
        if (idToken == null) {
            throw new InvalidTokenException("Invalid Google ID token. Unable to authenticate.");
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String password;
        if (!userService.existsByEmail(email)) {
            password = generateRandomPassword(12);
            RegisterDTO registerDTO = new RegisterDTO(name, email, password);
            registerCoder(registerDTO);
        }
        return createLoginResponse(userService.loadUserByUsername(email));
    }

    public LoginResDTO githubLogin(GithubLoginDTO githubLoginDto) {
        String authorizationCode = githubLoginDto.getCode();
        String accessToken = retrieveAccessToken(authorizationCode);
        Map body = retrieveUserDetails(accessToken);
        List<Map<String, Object>> emails= retrieveUserEmails(accessToken);
        String name = body.get("name").toString();
        if(emails.size()<1){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No email found");
        }
        String email = emails.get(0).get("email").toString();
        String password = "";
        if (!userService.existsByEmail(email)) {
            password = generateRandomPassword(12);
            RegisterDTO registerDTO = new RegisterDTO(name, email, password);
            registerCoder(registerDTO);
        }
        return createLoginResponse(userService.loadUserByUsername(email));
    }

    private String retrieveAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> tokenRequest = Map.of(
                "client_id", githubPropertiesConfig.clientId(),
                "client_secret", githubPropertiesConfig.clientSecret(),
                "code", authorizationCode
        );
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(tokenRequest, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(githubPropertiesConfig.tokenUrl(), entity, Map.class);
        if (response.getBody().containsKey("error")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve access token from GitHub.");
        }
        return (String) response.getBody().get("access_token");
    }

    private Map retrieveUserDetails(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> userRequest = new HttpEntity<>(headers);
        ResponseEntity<Map> userResponse = restTemplate.exchange(githubPropertiesConfig.userUrl(), HttpMethod.GET, userRequest, Map.class);
        if (userResponse.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve user information from GitHub.");
        }
        return userResponse.getBody();
    }

    private List<Map<String, Object>> retrieveUserEmails(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> emailRequest = new HttpEntity<>(headers);
        ResponseEntity<List> emailResponse = restTemplate.exchange(githubPropertiesConfig.emailUrl(), HttpMethod.GET, emailRequest, List.class);
        if (emailResponse.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve user emails from GitHub.");
        }
        return emailResponse.getBody();
    }

    public void registerUser(User user) throws DataIntegrityViolationException {
        if (userService.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Error saving user: " + e.getMessage());
        }
    }

    @Transactional
    public void registerAdmin(RegisterDTO registerDTO) throws DataIntegrityViolationException {
        User user = userMapper.toUser(registerDTO);
        user.setRoleId(UserRole.ROLE_ADMIN.getValue());
        registerUser(user);
    }

    @Transactional
    public void registerCoder(RegisterDTO registerDTO) {
        User user = userMapper.toUser(registerDTO);
        user.setRoleId(UserRole.ROLE_CODER.getValue());
        registerUser(user);
    }


    public CustomUserDetails getCurrentUserDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("User is not authenticated");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }

}
