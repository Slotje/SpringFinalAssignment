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
    @Autowired // Injecting UserService bean
    private UserService service;

    // Method to get the user's profile details
    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserProfile(
            Authentication authentication) {
        String username = authentication.getName();
        UserResponse response = service.getUserProfile(username);
        return ResponseEntity.ok(response);
    }

    // Method to update the user's profile details
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody UpdateUserRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        User updatedUser = service.updateUser(username, request, authentication);
        UserResponse response = UserResponse.from(updatedUser);
        return ResponseEntity.ok(response);
    }
}

















