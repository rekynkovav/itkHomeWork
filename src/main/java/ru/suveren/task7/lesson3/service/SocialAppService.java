package ru.suveren.task7.lesson3.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.suveren.task7.lesson3.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService service;
    private static final Logger logger = LoggerFactory.getLogger(SocialAppService.class);

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = extractEmail(attributes, provider);
        String name = extractName(attributes, provider);
        String providerId = extractProviderId(attributes, provider);

        logger.info("Аутентификация пользователя: {}, провайдер: {}, email: {}", name, provider, email);

        User user = service.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);

                    Set<String> roles = determineRoles(email, attributes, provider);
                    newUser.setRoles(roles);

                    logger.info("Создан новый пользователь: {} с ролями: {}", email, roles);
                    return service.save(newUser);
                });

        user.setLastLoginAt(LocalDateTime.now());
        service.save(user);

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

        logger.info("Пользователь {} успешно аутентифицирован. Роли: {}", user.getEmail(), user.getRoles());

        return new DefaultOAuth2User(authorities, attributes, "sub");
    }

    private String extractEmail(Map<String, Object> attributes, String provider) {
        if ("google".equals(provider)) {
            return (String) attributes.get("email");
        } else if ("github".equals(provider)) {
            return (String) attributes.get("email");
        }
        return null;
    }

    private String extractName(Map<String, Object> attributes, String provider) {
        if ("google".equals(provider)) {
            return (String) attributes.get("name");
        } else if ("github".equals(provider)) {
            return (String) attributes.get("login");
        }
        return null;
    }

    private String extractProviderId(Map<String, Object> attributes, String provider) {
        if ("google".equals(provider)) {
            return (String) attributes.get("sub");
        } else if ("github".equals(provider)) {
            return attributes.get("id").toString();
        }
        return null;
    }

    private Set<String> determineRoles(String email, Map<String, Object> attributes, String provider) {
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        if (isAdmin(email)) {
            roles.add("ADMIN");
            logger.info("Пользователю {} назначена роль ADMIN", email);
        }

        return roles;
    }

    private boolean isAdmin(String email) {

        if (email != null && email.endsWith("@javacode.com")) {
            return true;
        }

        if ("admin@example.com".equals(email)) {
            return true;
        }
        return false;
    }
}
