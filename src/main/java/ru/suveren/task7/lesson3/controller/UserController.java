package ru.suveren.task7.lesson3.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.suveren.task7.lesson3.model.User;
import ru.suveren.task7.lesson3.repository.UserRepository;

import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal OAuth2User oAuth2User,
                          Authentication authentication) {
        if (oAuth2User == null) {
            return "redirect:/login";
        }

        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            String login = (String) attributes.get("login");
            String picture = (String) attributes.get("picture");

            User user = userRepository.findByEmail(email).orElse(null);

            model.addAttribute("name", name != null ? name : login);
            model.addAttribute("email", email);
            model.addAttribute("login", login);
            model.addAttribute("picture", picture);
            model.addAttribute("roles", user != null ? user.getRoles() : null);
            model.addAttribute("authorities", authentication.getAuthorities());

            logger.info("Отображен профиль пользователя: {}", email);

        } catch (Exception e) {
            logger.error("Ошибка при загрузке профиля: {}", e.getMessage());
            model.addAttribute("error", "Ошибка загрузки профиля");
        }

        return "profile";
    }

    @GetMapping("/info")
    @ResponseBody
    public Map<String, Object> userInfo(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            logger.debug("Запрос информации о пользователе: {}");
            return oAuth2User.getAttributes();
        }
        return Map.of("error", "User not authenticated");
    }
}
