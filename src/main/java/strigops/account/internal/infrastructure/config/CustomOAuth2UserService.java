package strigops.account.internal.infrastructure.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import strigops.account.features.identity.entity.SocialAccounts;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.entity.extension.UserStatus;
import strigops.account.features.identity.repository.SocialAccountsRepository;
import strigops.account.features.identity.repository.UsersRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;
    private final SocialAccountsRepository socialAccountsRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        String providerId = Optional.ofNullable(oauth2User.getAttribute("sub"))
                .map(Object::toString)
                .orElseGet(() -> {
                    Object id = oauth2User.getAttribute("id");
                    if (id == null) throw new OAuth2AuthenticationException("Provider ID not found");
                    return id.toString();
                });

        String email = oauth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not provided by OAuth2 provider");
        }

        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        log.info("Processing OAuth2 login for provider: {}, email: {}", provider, email);

        return socialAccountsRepository.findByProviderAndProviderUserId(provider, providerId)
                .map(social -> {
                    UsersEntity user = social.getUser();
                    return new CustomOAuth2User(oauth2User, user.getId(), user.getEmail());
                })
                .orElseGet(() -> {
                    UsersEntity user = processUserAndSocial(email, name, picture, provider, providerId, oauth2User);
                    return new CustomOAuth2User(oauth2User, user.getId(), user.getEmail());
                });
    }

    private UsersEntity processUserAndSocial(String email, String name, String picture,
                                             String provider, String providerId, OAuth2User oauth2User) {

        UsersEntity user = usersRepository.findByEmail(email.toLowerCase())
                .orElseGet(() -> {
                    log.info("Creating new user for OAuth2: {}", email);
                    return usersRepository.save(UsersEntity.builder()
                            .email(email.toLowerCase())
                            .username(name)
                            .status(UserStatus.ACTIVE)
                            .build());
                });

        SocialAccounts socialAccount = SocialAccounts.builder()
                .user(user)
                .provider(provider)
                .providerUserId(providerId)
                .username(name)
                .email(email.toLowerCase())
                .profilePictureUrl(picture)
                .accessToken("")
                .build();

        socialAccountsRepository.save(socialAccount);
        log.info("Linked social account for user: {}", email);

        return user;
    }
}