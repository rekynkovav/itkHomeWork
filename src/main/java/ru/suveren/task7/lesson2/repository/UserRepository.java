package ru.suveren.task7.lesson2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.suveren.task7.lesson2.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
