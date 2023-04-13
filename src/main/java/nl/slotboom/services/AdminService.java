package nl.slotboom.services;

import nl.slotboom.exceptions.AppException;
import nl.slotboom.models.Role;
import nl.slotboom.models.User;
import nl.slotboom.models.requests.UpdateUserRoleRequest;
import nl.slotboom.models.responses.UserResponse;
import nl.slotboom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserResponseByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        return UserResponse.from(user);
    }

    public List<UserResponse> getAllUserResponses() {
        var users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = UserResponse.from(user);
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    public User updateUserRole(String username, UpdateUserRoleRequest updateUserRoleRequest, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (currentUser.getRole() != Role.ADMIN) {
            throw new AppException("Only admins can update user roles", HttpStatus.FORBIDDEN);
        }

        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (existingUser.getRole() == Role.ADMIN) {
            throw new AppException("Cannot update an admin's role", HttpStatus.METHOD_NOT_ALLOWED);
        }

        existingUser.setRole(updateUserRoleRequest.getRole());
        return userRepository.save(existingUser);
    }
}

