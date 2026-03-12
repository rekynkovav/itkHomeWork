package ru.suveren.task5.lesson1.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.suveren.task5.lesson1.dto.request.UserCreateRequest;
import ru.suveren.task5.lesson1.dto.request.UserUpdateRequest;
import ru.suveren.task5.lesson1.model.User;
import ru.suveren.task5.lesson1.model.Views;
import ru.suveren.task5.lesson1.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/users")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    @JsonView(Views.UserDetails.class)
    public ResponseEntity<User> getUser(@PathVariable Long id) {

        User user = userService.getUserById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Пользователь с таким id %d не найден!", id)
        ));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = modelMapper.map(request, User.class);
        return userService.save(user);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setId(id);
        userService.upDateUserFields(id, user.getEmail());
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Пользователь с id %d не найден", id)
                ));
        userService.deleteById(user.getId());
    }
}
