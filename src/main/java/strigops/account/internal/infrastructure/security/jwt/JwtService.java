package strigops.account.internal.infrastructure.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtService(
            JwtConfig jwtConfig,
            PrivateKey privateKey,
            PublicKey publicKey
    ) {
        this.jwtConfig = jwtConfig;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String generateAccessToken(String userId, String role) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userId)
                .issuer(jwtConfig.getIssuer())
                .audience().add(jwtConfig.getAudience()).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtConfig.getAccess().getExpiration())))
                .id(UUID.randomUUID().toString())
                .claim("role", role)
                .signWith(privateKey) // Otomatis deteksi RS256 dari tipe PrivateKey
                .compact();
    }

    public String generateRefreshToken(String userId) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userId)
                .issuer(jwtConfig.getIssuer())
                .audience().add(jwtConfig.getAudience()).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtConfig.getRefresh().getExpiration())))
                .id(UUID.randomUUID().toString())
                .claim("type", "refresh")
                .signWith(privateKey)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey) // Gunakan verifyWith untuk PublicKey
                    .requireIssuer(jwtConfig.getIssuer())
                    .requireAudience(jwtConfig.getAudience())
                    .build()
                    .parseSignedClaims(token) // Untuk JWT bertanda tangan (JWS)
                    .getPayload();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            // Biarkan dilempar ke filter untuk dihandle
            throw e;
        }
    }

    public String getUserId(String token) {
        return validateToken(token).getSubject();
    }

    public String getRole(String token) {
        return validateToken(token).get("role", String.class);
    }
}