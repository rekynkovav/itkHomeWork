package task5.lesson3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.suveren.task5.lesson3.controller.ProductController;
import ru.suveren.task5.lesson3.dto.request.ProductRequest;
import ru.suveren.task5.lesson3.dto.response.ProductResponse;
import ru.suveren.task5.lesson3.model.Product;
import ru.suveren.task5.lesson3.service.impl.ProductServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;
    private ProductRequest validRequest;
    private ProductResponse productResponse;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        validRequest = new ProductRequest();
        validRequest.setName("Смартфон");
        validRequest.setPrice(50000.0);

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Смартфон");
        productResponse.setDescription("Новый смартфон");
        productResponse.setPrice(50000.0);
        productResponse.setQuantityInStock(10);

        savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Смартфон");
        savedProduct.setDescription("Новый смартфон");
        savedProduct.setPrice(50000.0);
        savedProduct.setQuantityInStock(10);
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductResponse> expectedPage = new PageImpl<>(List.of(productResponse));

        when(productService.findAll(pageable)).thenReturn(expectedPage);

        // Act
        ResponseEntity<?> response = productController.getAllCustomer(pageable);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPage);
        verify(productService, times(1)).findAll(pageable);
    }

    @Test
    void createProduct_WithValidData_ShouldReturnSuccessMessage() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.save(any(ProductRequest.class))).thenReturn(savedProduct);

        // Act
        ResponseEntity<?> response = productController.creatCustomer(validRequest, bindingResult);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("покупатель сохранен");
        verify(productService, times(1)).save(validRequest);
    }

    @Test
    void createProduct_WithValidationErrors_ShouldReturnBadRequest() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<?> response = productController.creatCustomer(validRequest, bindingResult);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("некорректные данные");
        verify(productService, never()).save(any());
    }

    @Test
    void deleteProductById_WhenProductExists_ShouldReturnSuccessMessage() {
        // Arrange
        Long id = 1L;
        when(productService.existsById(id)).thenReturn(true);
        doNothing().when(productService).deleteById(id);

        // Act
        ResponseEntity<?> response = productController.deleteCustomerById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Книга удалена");
        verify(productService, times(1)).existsById(id);
        verify(productService, times(1)).deleteById(id);
    }

    @Test
    void deleteProductById_WhenProductDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Long id = 999L;
        when(productService.existsById(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = productController.deleteCustomerById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Книга не найдена");
        verify(productService, times(1)).existsById(id);
        verify(productService, never()).deleteById(anyLong());
    }
}
