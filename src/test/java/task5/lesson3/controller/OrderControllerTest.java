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
import ru.suveren.task5.lesson3.controller.OrderController;
import ru.suveren.task5.lesson3.dto.request.OrderRequest;
import ru.suveren.task5.lesson3.dto.response.OrderResponse;
import ru.suveren.task5.lesson3.model.Customer;
import ru.suveren.task5.lesson3.model.Order;
import ru.suveren.task5.lesson3.model.Product;
import ru.suveren.task5.lesson3.service.impl.OrderServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper;
    private OrderRequest validRequest;
    private OrderResponse orderResponse;
    private Order savedOrder;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        validRequest = new OrderRequest();
        validRequest.setShippingAddress("ул. Ленина, д. 1");
        validRequest.setTotalPrice(1500.50);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Иван");

        Product product = new Product();
        product.setId(1L);
        product.setName("Телефон");

        orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        orderResponse.setCustomer(customer);
        orderResponse.setProducts(List.of(product));
        orderResponse.setOrderDate(LocalDateTime.now());
        orderResponse.setShippingAddress("ул. Ленина, д. 1");
        orderResponse.setTotalPrice(1500.50);
        orderResponse.setOrderStatus("NEW");

        savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomer(customer);
        savedOrder.setProducts(List.of(product));
        savedOrder.setOrderDate(LocalDateTime.now());
        savedOrder.setShippingAddress("ул. Ленина, д. 1");
        savedOrder.setTotalPrice(1500.50);
        savedOrder.setOrderStatus("NEW");
    }

    @Test
    void getAllOrders_ShouldReturnPageOfOrders() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<OrderResponse> expectedPage = new PageImpl<>(List.of(orderResponse));

        when(orderService.findAll(pageable)).thenReturn(expectedPage);

        // Act
        ResponseEntity<?> response = orderController.getAllCustomer(pageable);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedPage);
        verify(orderService, times(1)).findAll(pageable);
    }

    @Test
    void createOrder_WithValidData_ShouldReturnSuccessMessage() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(orderService.save(any(OrderRequest.class))).thenReturn(savedOrder);

        // Act
        ResponseEntity<?> response = orderController.creatCustomer(validRequest, bindingResult);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("покупатель сохранен");
        verify(orderService, times(1)).save(validRequest);
    }

    @Test
    void createOrder_WithValidationErrors_ShouldReturnBadRequest() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<?> response = orderController.creatCustomer(validRequest, bindingResult);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("некорректные данные");
        verify(orderService, never()).save(any());
    }

    @Test
    void deleteOrderById_WhenOrderExists_ShouldReturnSuccessMessage() {
        // Arrange
        Long id = 1L;
        when(orderService.existsById(id)).thenReturn(true);
        doNothing().when(orderService).deleteById(id);

        // Act
        ResponseEntity<?> response = orderController.deleteCustomerById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Книга удалена");
        verify(orderService, times(1)).existsById(id);
        verify(orderService, times(1)).deleteById(id);
    }

    @Test
    void deleteOrderById_WhenOrderDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Long id = 999L;
        when(orderService.existsById(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = orderController.deleteCustomerById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Книга не найдена");
        verify(orderService, times(1)).existsById(id);
        verify(orderService, never()).deleteById(anyLong());
    }
}
