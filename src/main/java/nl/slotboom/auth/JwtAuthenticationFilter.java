package nl.slotboom.auth;


import java.io.IOException;

import lombok.RequiredArgsConstructor;
import nl.slotboom.repositories.TokenRepository;
import nl.slotboom.services.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository repository;

    // doFilterInternal: Override the doFilterInternal method from OncePerRequestFilter to implement the JWT authentication logic
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Extract the JWT token from the "Authorization" header of the request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;
        // If the JWT token is not present or doesn't start with "Bearer ", continue with the filter chain without authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Extract the JWT token from the "Authorization" header
        jwt = authHeader.substring(7);
        // Extract the username from the JWT token using the "jwtService" dependency
        userName = jwtService.extractUsername(jwt);
        // If the username is not null and the user is not already authenticated
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details using the "userDetailsService" dependency
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            // Check if the token is valid and not expired using the "tokenRepository" dependency
            var isTokenValid = repository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            // If the token is valid, create a new authentication token and set it in the security context using the "SecurityContextHolder" class from Spring Security
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}