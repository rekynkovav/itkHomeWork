package ru.suveren.task5.lesson3.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suveren.task5.lesson3.dto.request.OrderRequest;
import ru.suveren.task5.lesson3.dto.response.OrderResponse;
import ru.suveren.task5.lesson3.model.Order;
import ru.suveren.task5.lesson3.repository.OrderRepository;
import ru.suveren.task5.lesson3.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Page<OrderResponse> findAll(Pageable pageable) {

        Page<Order> listPageable = orderRepository.findAll(pageable);

        List<OrderResponse> listOrderResponse = listPageable.get()
                .map(item -> objectMapper.convertValue(item, OrderResponse.class))
                .toList();
        return new PageImpl<>(listOrderResponse, listPageable.getPageable(), listPageable.getTotalElements());
    }

    @Override
    public Order save(OrderRequest request) {
        Order order = objectMapper.convertValue(request, Order.class);
        return orderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponse findOrderById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return objectMapper.convertValue(orderOptional.get(), OrderResponse.class);
    }

    @Override
    public boolean existsById(Long id){
        return orderRepository.existsById(id);
    }
}
