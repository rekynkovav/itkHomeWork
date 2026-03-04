package ru.suveren.task5.lesson2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.suveren.task5.lesson2.model.Book;

import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Long, Book> {

    Optional<Book> getBookById(Long id);

    @Modifying
    @Query("UPDATE Book b SET b.title = :title WHERE b.id = :id")
    void updateBook(@Param("id") Long id, @Param("title") String title);
}
