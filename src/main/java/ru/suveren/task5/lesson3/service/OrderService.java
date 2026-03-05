package ru.suveren.task5.lesson3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.suveren.task5.lesson3.dto.request.OrderRequest;
import ru.suveren.task5.lesson3.dto.response.OrderResponse;
import ru.suveren.task5.lesson3.model.Order;


public interface OrderService {

    Page<OrderResponse> findAll(Pageable pageable);
    Order save(OrderRequest request);
    void deleteById(Long id);
    OrderResponse findOrderById(Long id);
    boolean existsById(Long id);
}
