package strigops.account.features.auth.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strigops.account.features.auth.login.command.LoginCommand;
import strigops.account.features.auth.login.dto.LoginResponse;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.repository.UsersRepository;
import strigops.account.features.session.SessionService;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    @Transactional
    public LoginResponse login(LoginCommand command, String userAgent, String ip) {
        UsersEntity user = usersRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password!");
        }

        return sessionService.handleLogin(user, userAgent, ip);
    }
}