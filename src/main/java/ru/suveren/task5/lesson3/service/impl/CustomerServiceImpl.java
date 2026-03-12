package ru.suveren.task5.lesson3.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suveren.task5.lesson3.dto.request.CustomerRequest;
import ru.suveren.task5.lesson3.dto.response.CustomerResponse;
import ru.suveren.task5.lesson3.model.Customer;
import ru.suveren.task5.lesson3.repository.CustomerRepository;
import ru.suveren.task5.lesson3.service.CustomerService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Page<CustomerResponse> findAll(Pageable pageable) {

        Page<Customer> listPageable = customerRepository.findAll(pageable);

        List<CustomerResponse> listCustomerResponse = listPageable.get()
                .map(item -> objectMapper.convertValue(item, CustomerResponse.class))
                .toList();
        return new PageImpl<>(listCustomerResponse, listPageable.getPageable(), listPageable.getTotalElements());
    }

    @Override
    public Customer save(CustomerRequest request) {
        Customer customer = objectMapper.convertValue(request, Customer.class);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerResponse findCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return objectMapper.convertValue(customerOptional.get(), CustomerResponse.class);
    }

    @Override
    public boolean existsById(Long id){
        return customerRepository.existsById(id);
    }
}
