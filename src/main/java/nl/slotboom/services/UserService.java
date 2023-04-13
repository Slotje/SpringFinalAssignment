package nl.slotboom.services;

import nl.slotboom.exceptions.AppException;
import nl.slotboom.models.User;
import nl.slotboom.models.requests.UpdateUserRequest;
import nl.slotboom.models.responses.UserResponse;
import nl.slotboom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // getUserProfile: get the detail of user
    public UserResponse getUserProfile(String username) {
        // Retrieve the user from the repository, or throw an exception if not found
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        // Convert the user into a UserResponse and return it
        return UserResponse.from(user);
    }

    // updateUser: Update an existing user's information
    public User updateUser(String username, UpdateUserRequest updateUserRequest, Authentication authentication) {
        // Get the username of the currently authenticated user
        String currentUsername = authentication.getName();
        // Check if the current user is trying to update their own account
        if (!currentUsername.equals(username)) {
            throw new AppException("Cannot update another user's account", HttpStatus.METHOD_NOT_ALLOWED);
        }
        // Find the user in the database based on their username
        User existingUser = repository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        // Check if the user's username is being changed
        String newUsername = updateUserRequest.getUsername();
        if (!newUsername.equals(existingUser.getUsername())) {
            // If the new username is different, check if it already exists in the database
            Optional<User> userWithSameUsername = repository.findByUsername(newUsername);
            if (userWithSameUsername.isPresent()) {
                // If it already exists, throw an exception
                throw new AppException("Username already exists", HttpStatus.CONFLICT);
            }
            // If the new username is available, set it on the existing user object
            existingUser.setUsername(newUsername);
        }
        // Check if the user's password is being changed
        String newPassword = updateUserRequest.getPassword();
        if (newPassword != null) {
            // If a new password is provided, encode it and set it on the existing user object
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);
        }
        // Save the updated user object to the database and return it
        return repository.save(existingUser);
    }
}



