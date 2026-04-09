package strigops.account.features.session;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strigops.account.features.auth.login.dto.LoginResponse;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.entity.UsersSession;
import strigops.account.features.identity.repository.UsersSessionRepostory;
import strigops.account.internal.infrastructure.security.JwtService;

@Service
public class SessionService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsersSessionRepostory sessionRepository;

    @Transactional
    public LoginResponse handleLogin(UsersEntity user) {
        UUID sessionId = UUID.randomUUID();

        boolean isMfa = user.isMfaEnable();
        LocalDateTime expiry = isMfa ? LocalDateTime.now().plusMinutes(5) : LocalDateTime.now().plusDays(7);

        UsersSession session = UsersSession.builder()
                .sessionId(sessionId)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(expiry)
                .mfaVerified(!isMfa)
                .revoked(false)
                .build();

        sessionRepository.save(session);


        String access = jwtService.createAccessToken(user, sessionId.toString());
        String refresh = jwtService.createRefreshToken(sessionId.toString());

        return LoginResponse.success(user.getId(), user.getEmail(), access, refresh);
    }
}