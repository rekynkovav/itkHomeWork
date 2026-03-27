package ru.suveren.task6.lesson1.repository;

import ru.suveren.task6.lesson1.model.Book;

public interface BookRepository {

    int save(Book book);
    Book get(Long id);
    int update (Book book);
    void delete (Long id);
}
