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

    public AuthenticationResponse register(CreateUserRequest request) {
        var existingUser = repository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new AppException("User already exists", HttpStatus.CONFLICT);
        }
        LocalDateTime now = LocalDateTime.now();
        Date createdAt = Date.from(now.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date updatedAt = Date.from(now.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        var savedUser = repository.save(user);
        System.out.println("User created successfully");
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}

