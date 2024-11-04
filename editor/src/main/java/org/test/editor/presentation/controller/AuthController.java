package org.test.editor.presentation.controller;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.*;
import org.test.editor.core.service.AuthService;
import org.test.editor.infra.service.AuthServiceImpl;
import org.test.editor.util.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginDTO loginDto) {
        LoginResDTO loginResDTO =authService.login(loginDto);
        ApiResponse response = new ApiResponse("user login successfully", "success",loginResDTO);
        return ResponseEntity.ok(response);
    }

    @SneakyThrows
    @PostMapping("/google")
    public ResponseEntity<ApiResponse> googleLogin(@Valid @RequestBody GmailLoginDTO gmailLoginDto) {
        LoginResDTO loginResDTO =  authService.googleLogin(gmailLoginDto);
        ApiResponse response = new ApiResponse("user login successfully", "success",loginResDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/github")
    public ResponseEntity<ApiResponse> githubLogin(@Valid @RequestBody GithubLoginDTO githubLoginDTO) {
        LoginResDTO loginResDTO = authService.githubLogin(githubLoginDTO);
        ApiResponse response = new ApiResponse("user login successfully", "success",loginResDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-coder")
    public ResponseEntity<ApiResponse> registerCoder(@Valid @RequestBody RegisterDTO registerDTO) {
            authService.registerCoder(registerDTO);
            ApiResponse response = new ApiResponse("user registered successfully", "success");
            return ResponseEntity.ok(response);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse> registerAdmin(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.registerAdmin(registerDTO);
        ApiResponse response = new ApiResponse("user registered successfully", "success");
        return ResponseEntity.ok(response);
    }

}