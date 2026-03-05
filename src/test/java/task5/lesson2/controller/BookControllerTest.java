package task5.lesson2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.suveren.task5.lesson2.controller.BookController;
import ru.suveren.task5.lesson2.dto.request.BookCreateRequest;
import ru.suveren.task5.lesson2.dto.request.BookUpdateRequest;
import ru.suveren.task5.lesson2.model.Book;
import ru.suveren.task5.lesson2.service.BookService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper;
    private Book testBook;
    private BookCreateRequest createRequest;
    private BookUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Война и мир");

        createRequest = new BookCreateRequest();
        createRequest.setTitle("Преступление и наказание");

        updateRequest = new BookUpdateRequest();
        updateRequest.setTitle("Анна Каренина");
   }

    @Test
    @DisplayName("Тест получения всех книг с пагинацией")
    void getAllBooks_WithPagination_ShouldReturnPageOfBooks() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        List<Book> books = List.of(testBook);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookService.findAll(any(Pageable.class))).thenReturn(bookPage);

        ResponseEntity<Page<Book>> response = bookController.getAllBooks(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("Война и мир");

        verify(bookService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Тест получения всех книг с кастомными параметрами пагинации")
    void getAllBooks_WithCustomPagination_ShouldReturnPageOfBooks() {
        Pageable pageable = PageRequest.of(2, 5, Sort.by("title").descending());
        List<Book> books = List.of(testBook);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 20);

        when(bookService.findAll(any(Pageable.class))).thenReturn(bookPage);

        ResponseEntity<Page<Book>> response = bookController.getAllBooks(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPageable().getPageNumber()).isEqualTo(2);
        assertThat(response.getBody().getPageable().getPageSize()).isEqualTo(5);
        assertThat(response.getBody().getTotalElements()).isEqualTo(20);

        verify(bookService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Тест получения книги по существующему ID")
    void getBookById_WithExistingId_ShouldReturnBook() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

        ResponseEntity<?> response = bookController.getBookById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(Book.class);

        Book returnedBook = (Book) response.getBody();
        assertThat(returnedBook.getId()).isEqualTo(1L);
        assertThat(returnedBook.getTitle()).isEqualTo("Война и мир");

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    @DisplayName("Тест получения книги по несуществующему ID")
    void getBookById_WithNonExistingId_ShouldReturnNotFound() {
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookController.getBookById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Книга не найдена");

        verify(bookService, times(1)).getBookById(99L);
    }

    @Test
    @DisplayName("Тест создания книги с валидными данными")
    void createBook_WithValidData_ShouldReturnSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(createRequest, Book.class)).thenReturn(testBook);
        when(bookService.save(any(Book.class))).thenReturn(testBook);

        ResponseEntity<?> response = bookController.createBook(createRequest, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Книга сохранена");

        verify(modelMapper, times(1)).map(createRequest, Book.class);
        verify(bookService, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Тест создания книги с невалидными данными")
    void createBook_WithInvalidData_ShouldReturnBadRequest() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = bookController.createBook(createRequest, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("некорректные данные");

        verify(modelMapper, never()).map(any(), any());
        verify(bookService, never()).save(any());
    }

    @Test
    @DisplayName("Тест создания книги при ошибке сервиса")
    void createBook_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(createRequest, Book.class)).thenReturn(testBook);
        when(bookService.save(any(Book.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = bookController.createBook(createRequest, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("ошибка на стороне сервера");

        verify(modelMapper, times(1)).map(createRequest, Book.class);
        verify(bookService, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Тест обновления книги с валидными данными")
    void updateBook_WithValidData_ShouldReturnSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(updateRequest, Book.class)).thenReturn(testBook);
        doNothing().when(bookService).updateBookFields(eq(1L), anyString());

        ResponseEntity<?> response = bookController.updateUser(1L, updateRequest, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Книга сохранена");

        verify(modelMapper, times(1)).map(updateRequest, Book.class);
        verify(bookService, times(1)).updateBookFields(eq(1L), eq(testBook.getTitle()));
    }

    @Test
    @DisplayName("Тест обновления книги с невалидными данными")
    void updateBook_WithInvalidData_ShouldReturnBadRequest() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = bookController.updateUser(1L, updateRequest, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("некорректные данные");

        verify(modelMapper, never()).map(any(), any());
        verify(bookService, never()).updateBookFields(any(), any());
    }

    @Test
    @DisplayName("Тест обновления книги при ошибке сервиса")
    void updateBook_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(updateRequest, Book.class)).thenReturn(testBook);
        doThrow(new RuntimeException("Update error")).when(bookService).updateBookFields(eq(1L), anyString());

        ResponseEntity<?> response = bookController.updateUser(1L, updateRequest, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("ошибка на стороне сервера");

        verify(modelMapper, times(1)).map(updateRequest, Book.class);
        verify(bookService, times(1)).updateBookFields(eq(1L), anyString());
    }

    @Test
    @DisplayName("Тест удаления существующей книги")
    void deleteBook_WithExistingId_ShouldReturnSuccess() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));
        doNothing().when(bookService).deleteById(1L);

        ResponseEntity<?> response = bookController.deleteUserById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Книга удалена");

        verify(bookService, times(1)).getBookById(1L);
        verify(bookService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Тест удаления несуществующей книги")
    void deleteBook_WithNonExistingId_ShouldReturnNotFound() {
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookController.deleteUserById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Книга не найдена");

        verify(bookService, times(1)).getBookById(99L);
        verify(bookService, never()).deleteById(any());
    }

    @Test
    @DisplayName("Тест удаления книги при ошибке сервиса")
    void deleteBook_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));
        doThrow(new RuntimeException("Delete error")).when(bookService).deleteById(1L);

        ResponseEntity<?> response = bookController.deleteUserById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Delete error");

        verify(bookService, times(1)).getBookById(1L);
        verify(bookService, times(1)).deleteById(1L);
    }
}