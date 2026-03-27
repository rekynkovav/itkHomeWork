package ru.suveren.task6.lesson1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @GetMapping("/home")
    public String getHome (){
        return "Home page";
    }
}
