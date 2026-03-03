package ru.suveren.task5.lesson2.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.suveren.task5.lesson2.model.Book;

import java.util.Optional;


public interface BookService {

    Page<Book> findAll(Pageable pageable);
    Book save(Book book);
    void deleteById(Long id);
    Optional<Book> getBookById(Long id);
    void updateBookFields(Long id, String title);
}
