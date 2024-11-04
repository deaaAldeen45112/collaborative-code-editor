package org.test.editor.core.service;


import org.test.editor.core.dto.*;
import org.test.editor.core.exception.InvalidTokenException;
import org.test.editor.core.model.User;
import org.test.editor.util.CustomUserDetails;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthService {

    LoginResDTO login(LoginDTO loginDto);
    LoginResDTO googleLogin(GmailLoginDTO gmailLoginDto) throws InvalidTokenException, GeneralSecurityException, IOException;
    LoginResDTO githubLogin(GithubLoginDTO githubLoginDto);
    void registerUser(User user);
    void registerAdmin(RegisterDTO registerDTO);
    void registerCoder(RegisterDTO registerDTO);
    CustomUserDetails getCurrentUserDetails();
}