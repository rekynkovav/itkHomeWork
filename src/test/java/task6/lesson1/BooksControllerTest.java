package task6.lesson1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.suveren.task6.lesson1.controller.BooksController;
import ru.suveren.task6.lesson1.model.Author;
import ru.suveren.task6.lesson1.model.Book;
import ru.suveren.task6.lesson1.service.BookService;

import java.nio.charset.StandardCharsets;
import java.time.Year;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class BooksControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BooksController booksController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Book testBook;
    private Author testAuthor;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders
                .standaloneSetup(booksController)
                .setMessageConverters(
                        new MappingJackson2HttpMessageConverter(objectMapper),
                        new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .build();

        testAuthor = new Author(1L, "Лев Толстой");
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Война и мир");
        testBook.setAuthor(testAuthor);
        testBook.setPublicationYear(Year.of(1869));
    }

    @Test
    void save_ShouldReturnOk_WhenBookSavedSuccessfully() throws Exception {
        when(bookService.save(any(Book.class))).thenReturn(true);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().string("книга сохранена"));

        verify(bookService, times(1)).save(any(Book.class));
    }

    @Test
    void save_ShouldReturnServerError_WhenExceptionThrown() throws Exception {
        when(bookService.save(any(Book.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().string("ошибка на стороне сервера"));

        verify(bookService, times(1)).save(any(Book.class));
    }

    @Test
    void get_ShouldReturnBook_WhenBookExists() throws Exception {
        when(bookService.get(anyLong())).thenReturn(testBook);

        mockMvc.perform(get("/books/{id}", 1L)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Война и мир"))
                .andExpect(jsonPath("$.author.id").value(1L))
                .andExpect(jsonPath("$.author.name").value("Лев Толстой"))
                .andExpect(jsonPath("$.publicationYear").value(1869));

        verify(bookService, times(1)).get(1L);
    }

    @Test
    void get_ShouldReturnNotFound_WhenBookDoesNotExist() throws Exception {
        when(bookService.get(anyLong())).thenReturn(null);

        mockMvc.perform(get("/books/{id}", 999L)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).get(999L);
    }

    @Test
    void get_ShouldReturnServerError_WhenExceptionThrown() throws Exception {
        when(bookService.get(anyLong())).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/books/{id}", 1L)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().string("ошибка на стороне сервера"));

        verify(bookService, times(1)).get(1L);
    }

    @Test
    void update_ShouldReturnOk_WhenBookUpdatedSuccessfully() throws Exception {
        when(bookService.update(any(Book.class))).thenReturn(true);

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("книга обновленна"));

        verify(bookService, times(1)).update(any(Book.class));
    }

    @Test
    void update_ShouldReturnNotFound_WhenBookDoesNotExist() throws Exception {
        when(bookService.update(any(Book.class))).thenReturn(false);

        mockMvc.perform(put("/books/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).update(any(Book.class));
    }

    @Test
    void update_ShouldReturnServerError_WhenExceptionThrown() throws Exception {
        when(bookService.update(any(Book.class))).thenThrow(new RuntimeException("Update failed"));

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testBook)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().string("ошибка на стороне сервера"));

        verify(bookService, times(1)).update(any(Book.class));
    }

    @Test
    void update_ShouldSetIdFromPath_WhenUpdating() throws Exception {
        Book bookWithoutId = new Book();
        bookWithoutId.setTitle("Война и мир");
        bookWithoutId.setAuthor(testAuthor);
        bookWithoutId.setPublicationYear(Year.of(1869));

        when(bookService.update(any(Book.class))).thenReturn(true);

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(bookWithoutId)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookService).update(argThat(book ->
                book.getId() == 1L &&
                book.getTitle().equals("Война и мир")
        ));
    }

    @Test
    void delete_ShouldReturnOk_WhenBookDeletedSuccessfully() throws Exception {
        doNothing().when(bookService).delete(anyLong());

        mockMvc.perform(delete("/books/{id}", 1L)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().string("Книга удалена"));

        verify(bookService, times(1)).delete(1L);
    }

    @Test
    void delete_ShouldReturnServerError_WhenExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Delete failed")).when(bookService).delete(anyLong());

        mockMvc.perform(delete("/books/{id}", 1L)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Delete failed"));

        verify(bookService, times(1)).delete(1L);
    }
}