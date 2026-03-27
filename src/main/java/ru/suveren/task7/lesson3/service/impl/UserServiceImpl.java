package ru.suveren.task7.lesson3.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import ru.suveren.task7.lesson3.model.User;
import ru.suveren.task7.lesson3.repository.UserRepository;
import ru.suveren.task7.lesson3.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
        return repository.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }
}
