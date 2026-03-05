package ru.suveren.task5.lesson3.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suveren.task5.lesson3.dto.request.OrderRequest;
import ru.suveren.task5.lesson3.dto.request.ProductRequest;
import ru.suveren.task5.lesson3.dto.response.OrderResponse;
import ru.suveren.task5.lesson3.dto.response.ProductResponse;
import ru.suveren.task5.lesson3.model.Order;
import ru.suveren.task5.lesson3.model.Product;
import ru.suveren.task5.lesson3.repository.ProductRepository;
import ru.suveren.task5.lesson3.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {

        Page<Product> listPageable = productRepository.findAll(pageable);

        List<ProductResponse> listProductResponse = listPageable.get()
                .map(item -> objectMapper.convertValue(item, ProductResponse.class))
                .toList();
        return new PageImpl<>(listProductResponse, listPageable.getPageable(), listPageable.getTotalElements());
    }

    @Override
    public Product save(ProductRequest request) {
        Product product = objectMapper.convertValue(request, Product.class);
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse findProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return objectMapper.convertValue(productOptional.get(), ProductResponse.class);
    }

    @Override
    public boolean existsById(Long id){
        return productRepository.existsById(id);
    }
}
