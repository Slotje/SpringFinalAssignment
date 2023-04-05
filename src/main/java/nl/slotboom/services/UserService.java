package nl.slotboom.services;

import nl.slotboom.models.User;
import nl.slotboom.repositories.UserRepository;
import nl.slotboom.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<String> login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        String token = jwtTokenUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<String> register(String username, String password) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with same username already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());
        userRepository.save(newUser);
        return ResponseEntity.ok("User created successfully");
    }

    public String getUsernameFromToken(String token) {
        return jwtTokenUtil.getUsernameFromToken(token);
    }

    public boolean validateToken(String token) {
        return jwtTokenUtil.validateToken(token);
    }
}

