package ru.suveren.task5.lesson1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.suveren.task5.lesson1.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("UPDATE Author u SET u.email = :email WHERE u.id = :id")
    int updateUserFields(@Param("id") Long id,
                         @Param("email") String email);

    @Query("SELECT u FROM Author u WHERE u.id = :id")
    Optional<User> getUserById(@Param("id") Long id);
}
