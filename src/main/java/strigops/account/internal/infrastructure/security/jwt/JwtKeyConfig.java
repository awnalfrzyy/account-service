package strigops.account.internal.infrastructure.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import java.security.PrivateKey;

@Configuration
public class JwtKeyConfig {

    private final JwtConfig jwtConfig;

    public JwtKeyConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public PrivateKey privateKey() throws Exception {
        // Lu pakai jwt.keys.private-key di properties kan?
        // Maka aksesnya: jwtConfig.getKeys().getPrivateKey()
        String path = jwtConfig.getKeys().getPrivateKey();
        String key = new String(Files.readAllBytes(Paths.get(path)));

        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    @Bean
    public PublicKey publicKey() throws Exception {
        String path = jwtConfig.getKeys().getPublicKey();
        String key = new String(Files.readAllBytes(Paths.get(path)));

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
}
