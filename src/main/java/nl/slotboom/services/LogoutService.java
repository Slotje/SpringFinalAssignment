package nl.slotboom.services;

import lombok.RequiredArgsConstructor;
import nl.slotboom.repositories.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    // The logout method in the provided code handles the logout request and invalidates the user's authentication and
    // authorization by setting the token's expired and revoked properties to true, saving the modified token to the
    // TokenRepository, and clearing the user's security context
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        // If the token is not present or does not start with the Bearer prefix, the method returns without anything
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        // If the token is present, the method retrieves it from the tokenRepository
        // and sets its expired and revoked properties to true
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            // The modified token is saved to the tokenRepository
            tokenRepository.save(storedToken);
            // The users security context is cleared using the SecurityContextHolder
            SecurityContextHolder.clearContext();
        }
    }
}

