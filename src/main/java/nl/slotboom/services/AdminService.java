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

    // getUserResponseByUsername: search user by the given username (admin)
    public UserResponse getUserResponseByUsername(String username) {
        // Find the user in the repository using the given username, throwing an exception if not found
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        // Create a UserResponse object from the found User object and return it
        return UserResponse.from(user);
    }

    // getAllUserResponses: get all users (admin)
    public List<UserResponse> getAllUserResponses() {
        // Retrieve all User objects from the repository
        var users = userRepository.findAll();
        // Create an empty list to hold the UserResponse objects
        List<UserResponse> userResponses = new ArrayList<>();
        // Iterate over each User object, creating a UserResponse object from it, and adding it to the list
        for (User user : users) {
            UserResponse userResponse = UserResponse.from(user);
            userResponses.add(userResponse);
        }
        // Return the list of UserResponse objects
        return userResponses;
    }

    // updateUserRole: used to update the role of a user to an admin (admin)
    public User updateUserRole(String username, UpdateUserRoleRequest updateUserRoleRequest, Authentication authentication) {
        // Get the username of the currently authenticated user
        String currentUsername = authentication.getName();
        // Find the user object of the currently authenticated user, throwing an exception if not found
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        // Check if the current user has admin role, throwing an exception if not
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AppException("Only admins can update user roles", HttpStatus.FORBIDDEN);
        }
        // Find the user object of the user to be updated, throwing an exception if not found
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        // Check if the user to be updated has admin role, throwing an exception if so
        if (existingUser.getRole() == Role.ADMIN) {
            throw new AppException("Cannot update an admin's role", HttpStatus.METHOD_NOT_ALLOWED);
        }
        // Set the new role for the user and save the changes to the database
        existingUser.setRole(updateUserRoleRequest.getRole());
        return userRepository.save(existingUser);
    }
}

