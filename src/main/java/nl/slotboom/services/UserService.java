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

    public UserResponse getUserProfile(String username) {
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return UserResponse.from(user);
    }

    public User updateUser(String username, UpdateUserRequest updateUserRequest, Authentication authentication) {
        String currentUsername = authentication.getName();
        if (!currentUsername.equals(username)) {
            throw new AppException("Cannot update another user's account", HttpStatus.METHOD_NOT_ALLOWED);
        }
        User existingUser = repository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        String newUsername = updateUserRequest.getUsername();
        if (!newUsername.equals(existingUser.getUsername())) { // check if username is being changed
            Optional<User> userWithSameUsername = repository.findByUsername(newUsername);
            if (userWithSameUsername.isPresent()) {
                throw new AppException("Username already exists", HttpStatus.CONFLICT);
            }
            existingUser.setUsername(newUsername);
        }

        String newPassword = updateUserRequest.getPassword();
        if (newPassword != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);
        }

        return repository.save(existingUser);
    }

}



