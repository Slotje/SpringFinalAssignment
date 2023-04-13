package nl.slotboom.services;


import lombok.RequiredArgsConstructor;
import nl.slotboom.exceptions.AppException;
import nl.slotboom.models.requests.AuthenticationRequest;
import nl.slotboom.models.responses.AuthenticationResponse;
import nl.slotboom.models.requests.CreateUserRequest;
import nl.slotboom.models.Role;
import nl.slotboom.models.User;
import nl.slotboom.repositories.TokenRepository;
import nl.slotboom.repositories.UserRepository;
import nl.slotboom.models.Token;
import nl.slotboom.models.TokenType;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // register: used to register an user
    public AuthenticationResponse register(CreateUserRequest request) {
        // Check if a user with the given username already exists in the repository
        var existingUser = repository.findByUsername(request.getUsername());
        // Throw an exception if a user with the given username already exists in the repository
        if (existingUser.isPresent()) {
            throw new AppException("User already exists", HttpStatus.CONFLICT);
        }
        // Set the createdAt and updatedAt fields to the current date and time
        LocalDateTime now = LocalDateTime.now();
        Date createdAt = Date.from(now.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date updatedAt = Date.from(now.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Create a new User object with the provided username, password, default role, and current date and time values
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        // Save the newly created User object to the repository
        var savedUser = repository.save(user);
        // Generate a JWT token for the newly created User object
        var jwtToken = jwtService.generateToken(user);
        // Save the generated JWT token to the repository
        saveUserToken(savedUser, jwtToken);
        // Return an AuthenticationResponse object containing the generated JWT token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // authenticate: used to authenticate an exisitng user to give JWTToken
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // The authenticate method receives an AuthenticationRequest object containing a username and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        // It retrieves the user associated with the provided username from a repository
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        // It generates a JWT token using a jwtService object
        var jwtToken = jwtService.generateToken(user);
        // It revokes all previous tokens for the user
        revokeAllUserTokens(user);
        // It saves the new token for the user
        saveUserToken(user, jwtToken);
        // It returns an AuthenticationResponse object containing the generated JWT token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // saveUserToken: used to save the token to the database
    private void saveUserToken(User user, String jwtToken) {
        // It creates a new Token object using the provided User object, JWT token, and other properties
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        // It saves the new token to a tokenRepository
        tokenRepository.save(token);
    }

    // revokeAllUserTokens: used to check the tokens of this user
    private void revokeAllUserTokens(User user) {
        // It retrieves all valid tokens associated with the provided user from a tokenRepository
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        // If no valid tokens are found, the method returns without making any changes
        if (validUserTokens.isEmpty())
            return;
        // If valid tokens are found, the method sets their expired and revoked properties to true
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        // It saves the modified tokens to the tokenRepository
        tokenRepository.saveAll(validUserTokens);
    }
}

