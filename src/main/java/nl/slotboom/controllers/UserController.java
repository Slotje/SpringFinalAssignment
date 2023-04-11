package nl.slotboom.controllers;

import nl.slotboom.models.User;
import nl.slotboom.models.requests.UpdateUserRequest;
import nl.slotboom.models.responses.UserResponse;
import nl.slotboom.repositories.UserRepository;
import nl.slotboom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import static nl.slotboom.constants.APIConstants.*;


@RestController
@RequestMapping("/" + API + "/" + VERSION + "/" + USER_ENDPOINT)
@PreAuthorize("isAuthenticated()")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserResponse userResponse = UserResponse.from(user);

        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String username, @RequestBody UpdateUserRequest updateUserRequest, Authentication authentication) {
        User updatedUser = userService.updateUser(username, updateUserRequest, authentication);

        UserResponse userResponse = UserResponse.from(updatedUser);
        return ResponseEntity.ok(userResponse);
    }

}
















