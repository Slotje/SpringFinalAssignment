package nl.slotboom.controllers;

import nl.slotboom.models.User;
import nl.slotboom.models.requests.UpdateUserRequest;
import nl.slotboom.models.responses.UserResponse;
import nl.slotboom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static nl.slotboom.constants.APIConstants.*;


@RestController
@RequestMapping("/" + API + "/" + VERSION + "/" + USER_ENDPOINT)
@PreAuthorize("isAuthenticated()")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserProfile(
            Authentication authentication) {
        String username = authentication.getName();
        UserResponse userResponse = service.getUserProfile(username);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody UpdateUserRequest updateUserRequest,
            Authentication authentication) {
        String username = authentication.getName();
        User updatedUser = service.updateUser(username, updateUserRequest, authentication);

        UserResponse userResponse = UserResponse.from(updatedUser);
        return ResponseEntity.ok(userResponse);
    }
}
















