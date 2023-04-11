package nl.slotboom.services;

import nl.slotboom.exceptions.UserNotAllowedException;
import nl.slotboom.models.User;
import nl.slotboom.models.requests.UpdateUserRequest;
import nl.slotboom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User updateUser(String username, UpdateUserRequest updateUserRequest, Authentication authentication) {
        String currentUsername = authentication.getName();
        if (!currentUsername.equals(username)) {
            throw new UserNotAllowedException("Cannot update another user's account");
        }

        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        existingUser.setUsername(updateUserRequest.getUsername());

        String newPassword = updateUserRequest.getPassword();
        if (newPassword != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);
        }

        return userRepository.save(existingUser);
    }
}



