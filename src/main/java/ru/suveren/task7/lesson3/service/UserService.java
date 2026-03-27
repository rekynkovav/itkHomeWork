package ru.suveren.task7.lesson3.service;

import ru.suveren.task7.lesson3.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    User save (User user);
}
