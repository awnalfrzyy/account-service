package strigops.account.features.session;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strigops.account.features.auth.login.dto.LoginResponse;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.entity.UsersSession;
import strigops.account.features.identity.repository.UsersSessionRepostory;
import strigops.account.features.security.mfa.MultiFactorService;
import strigops.account.internal.infrastructure.config.JwtService;

@Service
public class SessionService {

    @Inject
    JwtService jwtService;

    @Inject
    MultiFactorService mfaService;

    @Inject
    UsersSessionRepostory sessionRepository;

    @Transactional
    public LoginResponse handleLogin(UsersEntity user, String userAgent, String ip) {
        UUID sessionId = UUID.randomUUID();

        boolean isMfa = user.isMfaEnable();
        LocalDateTime expiry = isMfa ? LocalDateTime.now().plusMinutes(5) : LocalDateTime.now().plusDays(7);

        UsersSession session = UsersSession.builder()
                .sessionId(sessionId)
                .user(user)
                .userAgent(userAgent)
                .ipAddress(ip)
                .createdAt(LocalDateTime.now())
                .expiresAt(expiry)
                .mfaVerified(!isMfa)
                .revoked(false)
                .build();

        sessionRepository.save(session);

        if (isMfa) {
            String mfaToken = jwtService.createMfaToken(sessionId.toString());
            return LoginResponse.mfaRequired(mfaToken);
        }

        String access = jwtService.createAccessToken(user, sessionId.toString());
        String refresh = jwtService.CreateRefreshToken(sessionId.toString());

        return LoginResponse.success(user.getId(), user.getEmail(), access, refresh);
    }
}