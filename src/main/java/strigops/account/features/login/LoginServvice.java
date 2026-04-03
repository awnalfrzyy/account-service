package strigops.account.features.login;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import strigops.account.features.login.command.LoginCommand;
import strigops.account.features.login.command.LoginResult;
import strigops.account.internal.domain.entity.UsersEntity;
import strigops.account.internal.domain.repository.UsersRepository;
import strigops.account.internal.infrastructure.config.JwtService;

@Service
@RequiredArgsConstructor
public class LoginServvice {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public LoginResult login(LoginCommand command) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(command.getEmail(), command.getPassword())
        );

        // Get user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(command.getEmail());
        UsersEntity user = usersRepository.findByEmail(command.getEmail()).orElseThrow();

        // Generate JWT token
        String token = jwtService.generateToken(userDetails.getUsername());

        return new LoginResult(user.getId(), user.getEmail(), token);
    }
}
