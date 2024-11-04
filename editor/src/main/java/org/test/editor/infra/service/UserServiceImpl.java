package org.test.editor.infra.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.editor.core.dto.UserInvitationDto;
import org.test.editor.core.model.User;
import org.test.editor.core.service.UserService;
import org.test.editor.infra.repository.UserRepository;
import org.test.editor.util.CustomUserDetails;
import org.test.editor.util.constant.UserRole;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new CustomUserDetails(
                    user,
                    List.of(new SimpleGrantedAuthority(UserRole.fromValue(user.getRoleId()).name()))
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
    }

    public List<UserInvitationDto> getUsersByNameOrEmail(String searchTerm, Pageable pageable) {
        return userRepository.findUsersByNameOrEmail(searchTerm, pageable);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

