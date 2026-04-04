package strigops.account.internal.infrastructure.config;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final UUID userId;
    private final String email;

    public CustomOAuth2User(OAuth2User oauth2User, UUID userId, String email) {
        this.oauth2User = oauth2User;
        this.userId = userId;
        this.email = email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getName();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
    
    
}
