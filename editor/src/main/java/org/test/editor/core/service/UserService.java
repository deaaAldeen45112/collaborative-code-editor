package org.test.editor.core.service;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.test.editor.core.dto.UserInvitationDto;
import org.test.editor.core.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserInvitationDto> getUsersByNameOrEmail(String searchTerm, Pageable pageable);
    User saveUser(User user);
    List<User> getAllUsers();
    boolean existsByEmail(String email);
}