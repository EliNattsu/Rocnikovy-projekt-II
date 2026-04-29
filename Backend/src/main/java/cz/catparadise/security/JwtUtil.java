package cz.catparadise.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "catparadise-secret-key-must-be-at-least-32-chars";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 1 day

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generationToken(String emial, String role){
        return Jwts.builder()
                .subject(emial)
                .claim("role", role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }
    public String extractEmail(String token){
        return getClaims(token).getSubject();
    }
    public String extractRole(String token){
        return getClaims(token).get("role", String.class);
    }
    public boolean isTokenValid(String token){
        try {
            getClaims(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
