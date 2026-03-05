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
import ru.suveren.task5.lesson3.controller.CustomerController;
import ru.suveren.task5.lesson3.dto.request.CustomerRequest;
import ru.suveren.task5.lesson3.dto.response.CustomerResponse;
import ru.suveren.task5.lesson3.model.Customer;
import ru.suveren.task5.lesson3.service.impl.CustomerServiceImpl;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerServiceImpl customerService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CustomerController customerController;

    private ObjectMapper objectMapper;
    private CustomerRequest validRequest;
    private CustomerResponse customerResponse;
    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        validRequest = new CustomerRequest();
        validRequest.setFirstName("Иван");
        validRequest.setLastName("Петров");
        validRequest.setEmail("ivan@example.com");
        validRequest.setContactNumber("+12345678901");

        customerResponse = new CustomerResponse();
        customerResponse.setId(1L);
        customerResponse.setFirstName("Иван");
        customerResponse.setLastName("Петров");
        customerResponse.setEmail("ivan@example.com");
        customerResponse.setContactNumber("+12345678901");

        savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setFirstName("Иван");
        savedCustomer.setLastName("Петров");
        savedCustomer.setEmail("ivan@example.com");
        savedCustomer.setContactNumber("+12345678901");
    }

    @Test
    void getAllCustomers_ShouldReturnPageOfCustomers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<CustomerResponse> expectedPage = new PageImpl<>(List.of(customerResponse));

        when(customerService.findAll(pageable)).thenReturn(expectedPage);

        // Act
        ResponseEntity<?> response = customerController.getAllCustomer(pageable);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPage);
        verify(customerService, times(1)).findAll(pageable);
    }

    @Test
    void createCustomer_WithValidData_ShouldReturnSuccessMessage() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(customerService.save(any(CustomerRequest.class))).thenReturn(savedCustomer);

        // Act
        ResponseEntity<?> response = customerController.creatCustomer(validRequest, bindingResult);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("покупатель сохранен");
        verify(customerService, times(1)).save(validRequest);
    }

    @Test
    void createCustomer_WithValidationErrors_ShouldReturnBadRequest() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<?> response = customerController.creatCustomer(validRequest, bindingResult);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("некорректные данные");
        verify(customerService, never()).save(any());
    }

    @Test
    void createCustomer_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(customerService.save(any(CustomerRequest.class))).thenThrow(new RuntimeException("DB Error"));

        // Act
        ResponseEntity<?> response = customerController.creatCustomer(validRequest, bindingResult);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("ошибка на стороне сервера");
    }

    @Test
    void deleteCustomerById_WhenCustomerExists_ShouldReturnSuccessMessage() {
        // Arrange
        Long id = 1L;
        when(customerService.existsById(id)).thenReturn(true);
        doNothing().when(customerService).deleteById(id);

        // Act
        ResponseEntity<?> response = customerController.deleteCustomerById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Книга удалена");
        verify(customerService, times(1)).existsById(id);
        verify(customerService, times(1)).deleteById(id);
    }

    @Test
    void deleteCustomerById_WhenCustomerDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Long id = 999L;
        when(customerService.existsById(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = customerController.deleteCustomerById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Книга не найдена");
        verify(customerService, times(1)).existsById(id);
        verify(customerService, never()).deleteById(anyLong());
    }

    @Test
    void deleteCustomerById_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        // Arrange
        Long id = 1L;
        when(customerService.existsById(id)).thenThrow(new RuntimeException("DB Error"));

        // Act
        ResponseEntity<?> response = customerController.deleteCustomerById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(Objects.requireNonNull(response.getBody()).toString()).contains("DB Error");
    }
}
