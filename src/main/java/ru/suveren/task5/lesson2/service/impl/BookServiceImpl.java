package ru.suveren.task5.lesson2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.suveren.task5.lesson2.model.Book;
import ru.suveren.task5.lesson2.repository.BookRepository;
import ru.suveren.task5.lesson2.service.BookService;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.getBookById(id);
    }

    @Override
    public void updateBookFields(Long id, String title) {
        bookRepository.updateBook(id, title);
    }
}
