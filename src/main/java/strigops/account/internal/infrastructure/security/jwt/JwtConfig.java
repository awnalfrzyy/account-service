package strigops.account.internal.infrastructure.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String issuer;
    private String audience;
    private Token access = new Token();
    private Token refresh = new Token();
    private Keys keys = new Keys();

    @Getter
    @Setter
    public static class Token{
        private long expiration;
    }

    @Getter
    @Setter
    public static class Keys{
        private String privateKey;
        private String publicKey;
    }
}
