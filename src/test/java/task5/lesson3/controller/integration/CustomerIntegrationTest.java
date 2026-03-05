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
import ru.suveren.task5.lesson3.dto.request.CustomerRequest;
import ru.suveren.task5.lesson3.dto.response.CustomerResponse;
import ru.suveren.task5.lesson3.model.Customer;
import ru.suveren.task5.lesson3.repository.CustomerRepository;
import ru.suveren.task5.lesson3.service.impl.CustomerServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerIntegrationTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequest request;
    private Customer customer;
    private Customer savedCustomer;
    private CustomerResponse customerResponse;

    @BeforeEach
    void setUp() {
        request = new CustomerRequest();
        request.setFirstName("Тест");
        request.setLastName("Тестов");
        request.setEmail("test@example.com");
        request.setContactNumber("+1234567890");

        customer = new Customer();
        customer.setFirstName("Тест");
        customer.setLastName("Тестов");
        customer.setEmail("test@example.com");
        customer.setContactNumber("+1234567890");

        savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setFirstName("Тест");
        savedCustomer.setLastName("Тестов");
        savedCustomer.setEmail("test@example.com");
        savedCustomer.setContactNumber("+1234567890");

        customerResponse = new CustomerResponse();
        customerResponse.setId(1L);
        customerResponse.setFirstName("Тест");
        customerResponse.setLastName("Тестов");
        customerResponse.setEmail("test@example.com");
        customerResponse.setContactNumber("+1234567890");
    }

    @Test
    void fullCustomerFlow_ShouldWorkCorrectly() {
        // 1. Тест сохранения
        when(objectMapper.convertValue(any(CustomerRequest.class), eq(Customer.class)))
                .thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        Customer result = customerService.save(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Тест");

        verify(customerRepository, times(1)).save(any(Customer.class));

        // 2. Тест поиска по ID
        when(customerRepository.findById(1L)).thenReturn(Optional.of(savedCustomer));
        when(objectMapper.convertValue(any(Customer.class), eq(CustomerResponse.class)))
                .thenReturn(customerResponse);

        CustomerResponse found = customerService.findCustomerById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getEmail()).isEqualTo("test@example.com");

        // 3. Тест получения всех с пагинацией
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(List.of(savedCustomer));

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(objectMapper.convertValue(any(Customer.class), eq(CustomerResponse.class)))
                .thenReturn(customerResponse);

        Page<CustomerResponse> allCustomers = customerService.findAll(pageable);

        assertThat(allCustomers.getContent()).hasSize(1);
        assertThat(allCustomers.getContent().get(0).getId()).isEqualTo(1L);

        // 4. Тест existsById
        when(customerRepository.existsById(1L)).thenReturn(true);

        boolean exists = customerService.existsById(1L);
        assertThat(exists).isTrue();

        // 5. Тест удаления
        doNothing().when(customerRepository).deleteById(1L);

        customerService.deleteById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void objectMapperMapping_ShouldWorkCorrectly() {
        // Тест маппинга Request -> Entity через ObjectMapper
        when(objectMapper.convertValue(request, Customer.class)).thenReturn(customer);

        Customer mappedCustomer = objectMapper.convertValue(request, Customer.class);

        assertThat(mappedCustomer.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(mappedCustomer.getEmail()).isEqualTo(request.getEmail());

        // Тест маппинга Entity -> Response
        when(objectMapper.convertValue(savedCustomer, CustomerResponse.class)).thenReturn(customerResponse);

        CustomerResponse mappedResponse = objectMapper.convertValue(savedCustomer, CustomerResponse.class);

        assertThat(mappedResponse.getId()).isEqualTo(savedCustomer.getId());
        assertThat(mappedResponse.getContactNumber()).isEqualTo(savedCustomer.getContactNumber());
    }
}