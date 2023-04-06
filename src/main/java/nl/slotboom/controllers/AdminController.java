package nl.slotboom.controllers;

import nl.slotboom.models.User;
import nl.slotboom.models.responses.UserResponse;
import nl.slotboom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static nl.slotboom.constants.APIConstants.*;


@RestController
@RequestMapping("/" + API + "/" + VERSION + "/" + ADMIN_ENDPOINT)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserResponse userResponse = UserResponse.from(user);

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        var users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = UserResponse.from(user);
            userResponses.add(userResponse);
        }
        return userResponses;
    }

}