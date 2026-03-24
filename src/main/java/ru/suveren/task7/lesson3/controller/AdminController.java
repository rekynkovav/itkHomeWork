package ru.suveren.task7.lesson3.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.suveren.task7.lesson3.model.User;
import ru.suveren.task7.lesson3.repository.UserRepository;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        logger.info("Администратор запросил дашборд");
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("userCount", users.size());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        logger.info("Администратор запросил список пользователей");
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }
}
