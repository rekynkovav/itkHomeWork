package ru.suveren.task7.lesson3.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public String login() {
        logger.debug("Отображена страница входа");
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        logger.warn("Попытка доступа без прав");
        return "access-denied";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        logger.info("Пользователь успешно вышел из системы");
        return "logout-success";
    }
}
