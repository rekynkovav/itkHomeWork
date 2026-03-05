package ru.suveren.task5.lesson3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.suveren.task5.lesson3.dto.request.CustomerRequest;
import ru.suveren.task5.lesson3.dto.response.CustomerResponse;
import ru.suveren.task5.lesson3.model.Customer;

public interface CustomerService {

    Page<CustomerResponse> findAll(Pageable pageable);
    Customer save(CustomerRequest request);
    void deleteById(Long id);
    CustomerResponse findCustomerById(Long id);
    boolean existsById(Long id);
}
