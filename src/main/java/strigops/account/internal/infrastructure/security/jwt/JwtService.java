package strigops.account.internal.infrastructure.security.jwt;

import io.jsonwebtoken.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class JwtService {

    private final JwtConfig jwtConfig;
    private final PrivateKey privateKey;
    private  final PublicKey publicKey;

    public JwtService(
            JwtConfig jwtConfig,
            PrivateKey privateKey,
            PublicKey publicKey
            ){
        this.jwtConfig = jwtConfig;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String generateAccessToken(String userId, String role){
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuer(jwtConfig.getIssuer())
                .setAudience(jwtConfig.getAudience())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(jwtConfig.getAccess().getExpiration())))
                .setId(UUID.randomUUID().toString()) // jti
                .claim("role", role)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateRefreshToken(String userId){
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuer(jwtConfig.getIssuer())
                .setAudience(jwtConfig.getAudience())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(jwtConfig.getRefresh().getExpiration())))
                .setId(UUID.randomUUID().toString())
                .claim("type", "refresh")
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims validateToken(String token){
        return Jwts.parser()
                .setSigningKey(publicKey)
                .requireIssuer(jwtConfig.getIssuer())
                .requireAudience(jwtConfig.getAudience())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token){
        return validateToken(token).getSubject();
    }

    public String getRole(String token){
        return (String)validateToken(token).get("role");
    }
}

