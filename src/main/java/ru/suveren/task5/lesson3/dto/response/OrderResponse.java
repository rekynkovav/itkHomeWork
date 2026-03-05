package ru.suveren.task5.lesson3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.suveren.task5.lesson3.model.Customer;
import ru.suveren.task5.lesson3.model.Product;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Customer customer;
    private List<Product> products;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private Double totalPrice;
    private String orderStatus;
}

