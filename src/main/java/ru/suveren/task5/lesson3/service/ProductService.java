package ru.suveren.task5.lesson3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.suveren.task5.lesson3.dto.request.ProductRequest;
import ru.suveren.task5.lesson3.dto.response.ProductResponse;
import ru.suveren.task5.lesson3.model.Product;

public interface ProductService {

    Page<ProductResponse> findAll(Pageable pageable);
    Product save(ProductRequest request);
    void deleteById(Long id);
    ProductResponse findProductById(Long id);
    boolean existsById(Long id);
}
