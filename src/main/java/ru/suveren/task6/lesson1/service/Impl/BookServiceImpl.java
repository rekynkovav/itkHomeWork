package ru.suveren.task6.lesson1.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.suveren.task6.lesson1.model.Book;
import ru.suveren.task6.lesson1.repository.BookRepository;
import ru.suveren.task6.lesson1.service.BookService;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public boolean save(Book book) {
        boolean flag;
        int temp = bookRepository.save(book);
        flag = temp > 0;
        return flag;
    }

    @Override
    public Book get(Long id) {
        return bookRepository.get(id);
    }

    @Override
    public boolean update(Book book) {
        boolean flag;
        int temp = bookRepository.update(book);
        flag = temp > 0;
        return flag;
    }

    @Override
    public void delete(Long id) {
        bookRepository.delete(id);
    }
}
