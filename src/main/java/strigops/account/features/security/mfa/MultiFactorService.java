package strigops.account.features.security.mfa;

import jakarta.inject.Inject;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.transaction.annotation.Transactional;
import strigops.account.features.identity.repository.UsersRepository;
import java.util.UUID;


public class MultiFactorService {
    @Inject
    UsersRepository userRepository;

    public String generateNewSecret() {
        return Base32.random();
    }

    public boolean verifyCode(String secret, String code) {
        if (secret == null || code == null) return false;
        try {
            Totp totp = new Totp(secret);
            return totp.verify(code);
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void enableMfa(UUID userId, String secret, String code) {
        if (verifyCode(secret, code)) {
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setMfaSecret(secret);
            user.setMfaEnable(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Code MFA not valid!");
        }
    }
}
