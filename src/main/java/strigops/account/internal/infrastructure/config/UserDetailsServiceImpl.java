package strigops.account.internal.infrastructure.config;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import strigops.account.internal.domain.entity.UsersEntity;
import strigops.account.internal.domain.repository.UsersRepository;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String canonicalEmail = canonicalizeEmail(email);
        UsersEntity user = usersRepository.findByEmail(canonicalEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + canonicalEmail));

        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }

    private String canonicalizeEmail(String email) {
        if (email == null) {
            throw new UsernameNotFoundException("Email cannot be null");
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        if (normalizedEmail.isEmpty()) {
            throw new UsernameNotFoundException("Email cannot be empty");
        }
        return normalizedEmail;
    }
}
