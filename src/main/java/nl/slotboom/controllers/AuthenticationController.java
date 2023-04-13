package nl.slotboom.controllers;

import lombok.RequiredArgsConstructor;
import nl.slotboom.models.requests.AuthenticationRequest;
import nl.slotboom.models.responses.AuthenticationResponse;
import nl.slotboom.services.AuthenticationService;
import nl.slotboom.models.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nl.slotboom.constants.APIConstants.*;

@RestController
@RequestMapping("/" + API + "/" + VERSION + "/" + AUTH_ENDPOINT)
@RequiredArgsConstructor
public class AuthenticationController {

    // Injecting AuthenticationService bean
    @Autowired
    private final AuthenticationService service;

    // Mapping POST request to /register endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    // Mapping POST request to /authenticate endpoint for user authentication
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
