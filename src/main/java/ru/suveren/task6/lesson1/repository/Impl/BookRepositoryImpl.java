package ru.suveren.task6.lesson1.repository.Impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.suveren.task6.lesson1.model.Book;
import ru.suveren.task6.lesson1.repository.BookRepository;

@Repository
@AllArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String GET_BOOK = "SELECT * FROM books WHERE id = ?";
    private final String SAVE_BOOK = """
           INSERT INTO books (title, author, publicationYear)
           VALUES (?,?,?)
           """;
    private final String UPDATE_BOOK = """
                        UPDATE books
                        SET title = ?,
                            author = ?,
                            getPublicationYear = ?,
                        WHERE id = ?
            """;
    private final String DELETE_BOOK = "UPDATE books SET deleted = true WHERE id = ?";

    @Override
    public int save(Book book) {
        return jdbcTemplate.update(SAVE_BOOK,
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear()
        );
    }

    @Override
    public Book get(Long id) {
        return (Book) jdbcTemplate.query(GET_BOOK, new BeanPropertyRowMapper<>(Book.class), id);
    }

    @Override
    public int update(Book book) {
        return  jdbcTemplate.update(UPDATE_BOOK,
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BOOK, id);
    }
}
