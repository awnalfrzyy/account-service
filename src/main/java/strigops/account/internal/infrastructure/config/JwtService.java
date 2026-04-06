package strigops.account.internal.infrastructure.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import strigops.account.features.identity.entity.UsersEntity;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) {
            throw new IllegalArgumentException("Invalid token: unable to extract claims");
        }
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .clockSkewSeconds(5)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new IllegalArgumentException("Token expired", e);

        } catch (io.jsonwebtoken.JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }

    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    public String createMfaToken(String sessionId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "MFA_CHALLENGE");
        claims.put("sid", sessionId);

        return Jwts.builder()
                .claims(claims)
                .subject("mfa_user")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 300000))
                .signWith(getSigningKey())
                .compact();
    }

    public String createAccessToken(UsersEntity users, String sessionId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "ACCESS");
        claims.put("sid", sessionId);

        return Jwts.builder()
                .claims(claims)
                .subject(users.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String CreateRefreshToken(String sessionId){
        return Jwts.builder()
                .claim("sid", sessionId)
                .claim("type", "REFRESH")
                .setSubject("refresh_token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSigningKey())
                .compact();
    }

}
