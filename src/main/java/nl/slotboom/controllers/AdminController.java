package nl.slotboom.controllers;

import nl.slotboom.models.User;
import nl.slotboom.models.requests.UpdateUserRoleRequest;
import nl.slotboom.models.responses.UserResponse;
import nl.slotboom.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nl.slotboom.constants.APIConstants.*;


@RestController
@RequestMapping("/" + API + "/" + VERSION + "/" + ADMIN_ENDPOINT)
// Using Spring Security's PreAuthorize annotation to ensure that only users with ADMIN role can access the endpoints in this controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    // Injecting AdminService bean
    @Autowired
    private AdminService service;

    // Mapping GET request to /details/{username} endpoint to retrieve a specific users profile
    @GetMapping("/details/{username}")
    public ResponseEntity<UserResponse> getUserProfile(
            @PathVariable String username) {
        UserResponse response = service.getUserResponseByUsername(username);
        return ResponseEntity.ok(response);
    }

    // Mapping GET request to /details/all endpoint to retrieve details of all users
    @GetMapping("/details/all")
    public List<UserResponse> getAllUsers() {
        return service.getAllUserResponses();
    }

    // Mapping PUT request to /update_role/{username} endpoint to update a user's role
    @PutMapping("/update_role/{username}")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable String username,
            @RequestBody UpdateUserRoleRequest request,
            Authentication authentication) {
        // Calling AdminService method to update the user's role and get the updated User object
        User updatedUser = service.updateUserRole(username, request, authentication);
        UserResponse response = UserResponse.from(updatedUser);
        return ResponseEntity.ok(response);
    }
}

