package javasecurity.securityJWT.config;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private static final String SECRET_KEY = "tzga5j4T0z+JOkjlcTJx4gFiLklquj8y/TmhLmrw4eSIf9Zlo3Nn4XzGtyKWHz/k";

    public String extractUsername(String token) {
        return null;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }
    
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}
