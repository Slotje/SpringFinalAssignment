package nl.slotboom.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import nl.slotboom.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    // This variable contains the secret key used for JWT token generation and validation.
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // extractUsername: extracts the username from the JWT token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // extractClaim: extracts the specified claim from the JWT token.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // generateToken: generates a JWT token for the provided user details and additional claims.
    public String generateToken(UserDetails userDetails) {
        // The generateToken method creates a JWT token using the provided extra claims, user details, and a secret key
        User user = (User) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // It sets the token's subject to the username of the user, sets the token's expiration time to
        // 24 hours from the current time, and signs the token using the HMAC-SHA256 algorithm
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // isTokenValid: checks whether the provided JWT token is valid for the given user details.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // isTokenExpired: checks whether the provided JWT token has expired.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // extractExpiration: extracts the expiration date from the JWT token.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // extractAllClaims: extracts all claims from the JWT token.
    private Claims extractAllClaims(String token) {
        // It gets the token using the Jwts utility class and retrieves the token's body
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // getSignInKey: decodes and returns the secret key as a Key object that can be used to sign and validate the JWT token.
    private Key getSignInKey() {
        // It decodes the secret key from Base64 format to a byte array
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // It creates a Key object using the decoded byte array and returns it
        return Keys.hmacShaKeyFor(keyBytes);
    }
}