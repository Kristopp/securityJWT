package javasecurity.securityJWT.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;

@Service
public class JwtService {

    SecretKey key = Jwts.SIG.HS256.key().build();

    private Claims extractAllClaims(String token) {
        String jws = Jwts.builder().signWith(key).compact();

        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload();
            return claims;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid token", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Instant now = Instant.now();
        return Jwts
                .builder()
                .claim("userEmail", userDetails.getUsername())
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    // Validate if this token is expired or valid for this user
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

}
