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
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/details/{username}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String username) {
        UserResponse userResponse = adminService.getUserResponseByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUserResponses();
    }

    @PutMapping("/update_role/{username}")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable String username,
                                                       @RequestBody UpdateUserRoleRequest updateUserRoleRequest,
                                                       Authentication authentication) {
        User updatedUser = adminService.updateUserRole(username, updateUserRoleRequest, authentication);

        UserResponse userResponse = UserResponse.from(updatedUser);
        return ResponseEntity.ok(userResponse);
    }
}
