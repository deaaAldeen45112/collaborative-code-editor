package org.test.editor.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.UserInvitationDto;
import org.test.editor.core.service.UserService;
import org.test.editor.util.ApiResponse;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserInvitationDto>>> searchUsers(
            @RequestParam String searchTerm) {
        Pageable pageable = PageRequest.of(0, 1000);
        return ResponseEntity.ok(new ApiResponse(userService.getUsersByNameOrEmail(searchTerm, pageable)));
    }

}