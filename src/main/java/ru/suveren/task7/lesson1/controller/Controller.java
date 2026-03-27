package ru.suveren.task7.lesson1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/home")
    public String home() {
        return "home page";
    }

    @GetMapping("/all")
    public String all() {
        return "all page";
    }
}
