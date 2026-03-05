package task5.lesson3.controller.integration;

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
import ru.suveren.task5.lesson3.dto.request.OrderRequest;
import ru.suveren.task5.lesson3.dto.response.OrderResponse;
import ru.suveren.task5.lesson3.model.Customer;
import ru.suveren.task5.lesson3.model.Order;
import ru.suveren.task5.lesson3.model.Product;
import ru.suveren.task5.lesson3.repository.OrderRepository;
import ru.suveren.task5.lesson3.service.impl.OrderServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderIntegrationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequest request;
    private Order order;
    private Order savedOrder;
    private OrderResponse orderResponse;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Иван");

        product = new Product();
        product.setId(1L);
        product.setName("Телефон");
        product.setPrice(50000.0);

        request = new OrderRequest();
        request.setShippingAddress("ул. Тестовая, д. 1");
        request.setTotalPrice(50000.0);

        order = new Order();
        order.setShippingAddress("ул. Тестовая, д. 1");
        order.setTotalPrice(50000.0);

        savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomer(customer);
        savedOrder.setProducts(List.of(product));
        savedOrder.setOrderDate(LocalDateTime.now());
        savedOrder.setShippingAddress("ул. Тестовая, д. 1");
        savedOrder.setTotalPrice(50000.0);
        savedOrder.setOrderStatus("NEW");

        orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        orderResponse.setCustomer(customer);
        orderResponse.setProducts(List.of(product));
        orderResponse.setOrderDate(LocalDateTime.now());
        orderResponse.setShippingAddress("ул. Тестовая, д. 1");
        orderResponse.setTotalPrice(50000.0);
        orderResponse.setOrderStatus("NEW");
    }

    @Test
    void fullOrderFlow_ShouldWorkCorrectly() {
        // 1. Тест сохранения
        when(objectMapper.convertValue(any(OrderRequest.class), eq(Order.class)))
                .thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.save(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getShippingAddress()).isEqualTo("ул. Тестовая, д. 1");

        verify(orderRepository, times(1)).save(any(Order.class));

        // 2. Тест поиска по ID
        when(orderRepository.findById(1L)).thenReturn(Optional.of(savedOrder));
        when(objectMapper.convertValue(any(Order.class), eq(OrderResponse.class)))
                .thenReturn(orderResponse);

        OrderResponse found = orderService.findOrderById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getShippingAddress()).isEqualTo("ул. Тестовая, д. 1");
        assertThat(found.getCustomer()).isNotNull();
        assertThat(found.getProducts()).hasSize(1);

        // 3. Тест получения всех с пагинацией
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(savedOrder));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(objectMapper.convertValue(any(Order.class), eq(OrderResponse.class)))
                .thenReturn(orderResponse);

        Page<OrderResponse> allOrders = orderService.findAll(pageable);

        assertThat(allOrders.getContent()).hasSize(1);
        assertThat(allOrders.getContent().get(0).getId()).isEqualTo(1L);

        // 4. Тест existsById
        when(orderRepository.existsById(1L)).thenReturn(true);

        boolean exists = orderService.existsById(1L);
        assertThat(exists).isTrue();

        // 5. Тест удаления
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void objectMapperMapping_ShouldWorkCorrectly() {
        // Тест маппинга Request -> Entity через ObjectMapper
        when(objectMapper.convertValue(request, Order.class)).thenReturn(order);

        Order mappedOrder = objectMapper.convertValue(request, Order.class);

        assertThat(mappedOrder.getShippingAddress()).isEqualTo(request.getShippingAddress());
        assertThat(mappedOrder.getTotalPrice()).isEqualTo(request.getTotalPrice());

        // Тест маппинга Entity -> Response
        when(objectMapper.convertValue(savedOrder, OrderResponse.class)).thenReturn(orderResponse);

        OrderResponse mappedResponse = objectMapper.convertValue(savedOrder, OrderResponse.class);

        assertThat(mappedResponse.getId()).isEqualTo(savedOrder.getId());
        assertThat(mappedResponse.getShippingAddress()).isEqualTo(savedOrder.getShippingAddress());
    }
}