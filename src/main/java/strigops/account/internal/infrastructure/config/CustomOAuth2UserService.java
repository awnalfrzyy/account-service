package strigops.account.internal.infrastructure.config;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import strigops.account.internal.domain.entity.SosialAccounts;
import strigops.account.internal.domain.entity.UsersEntity;
import strigops.account.internal.domain.repository.SosialAccountsRepository;
import strigops.account.internal.domain.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;
    private final SosialAccountsRepository sosialAccountsRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = getOAuth2User(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Object id = oauth2User.getAttribute("id");
        if (id == null) {
            id = oauth2User.getAttribute("sub");
        }
        if (id == null){
            throw new OAuth2AuthenticationException("Provider ID not found");
        }
        System.out.println(oauth2User.getAttributes());
        String providerId = id.toString();
        String email = oauth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not provided by OAuth2 provider");
        }
        String name = oauth2User.getAttribute("name");

        Optional<SosialAccounts> existingSocial = sosialAccountsRepository.findByProviderAndProviderUserId(provider, providerId);

        if (existingSocial.isPresent()) {
            UsersEntity user = existingSocial.get().getUser();
            return new CustomOAuth2User(oauth2User, user.getId(), user.getEmail());
        }

        Optional<UsersEntity> existingUser = usersRepository.findByEmail(email.toLowerCase());
        UsersEntity user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = UsersEntity.builder()
                    .email(email.toLowerCase())
                    .username(name)
                    .active(true)
                    .build();
            user = usersRepository.save(user);
        }

        SosialAccounts socialAccount = SosialAccounts.builder()
                .user(user)
                .provider(provider)
                .providerUserId(providerId)
                .username(name)
                .email(email.toLowerCase())
                .profilePictureUrl(oauth2User.getAttribute("picture"))
                .accessToken("")
                .build();
        sosialAccountsRepository.save(socialAccount);

        return new CustomOAuth2User(oauth2User, user.getId(), user.getEmail());
    }

    protected OAuth2User getOAuth2User(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return super.loadUser(userRequest);
    }
}
