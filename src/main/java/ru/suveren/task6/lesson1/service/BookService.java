package ru.suveren.task6.lesson1.service;

import ru.suveren.task6.lesson1.model.Book;

public interface BookService {
    boolean save(Book book);
    Book get(Long id);
    boolean update (Book book);
    void delete (Long id);
}
