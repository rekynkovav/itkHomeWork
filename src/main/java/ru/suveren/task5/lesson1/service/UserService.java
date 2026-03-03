package ru.suveren.task5.lesson1.service;


import ru.suveren.task5.lesson1.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
    Optional<User> getUserById(Long id);
    int upDateUserFields(Long id, String email);
}
