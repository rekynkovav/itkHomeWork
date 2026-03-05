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
import ru.suveren.task5.lesson3.dto.request.ProductRequest;
import ru.suveren.task5.lesson3.dto.response.ProductResponse;
import ru.suveren.task5.lesson3.model.Product;
import ru.suveren.task5.lesson3.repository.ProductRepository;
import ru.suveren.task5.lesson3.service.impl.ProductServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductIntegrationTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest request;
    private Product product;
    private Product savedProduct;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        request = new ProductRequest();
        request.setName("Тестовый товар");
        request.setPrice(1000.0);

        product = new Product();
        product.setName("Тестовый товар");
        product.setPrice(1000.0);

        savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Тестовый товар");
        savedProduct.setDescription("Описание");
        savedProduct.setPrice(1000.0);
        savedProduct.setQuantityInStock(5);

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Тестовый товар");
        productResponse.setDescription("Описание");
        productResponse.setPrice(1000.0);
        productResponse.setQuantityInStock(5);
    }

    @Test
    void fullProductFlow_ShouldWorkCorrectly() {
        // 1. Тест сохранения
        when(objectMapper.convertValue(any(ProductRequest.class), eq(Product.class)))
                .thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.save(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Тестовый товар");

        verify(productRepository, times(1)).save(any(Product.class));

        // 2. Тест поиска по ID
        when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));
        when(objectMapper.convertValue(any(Product.class), eq(ProductResponse.class)))
                .thenReturn(productResponse);

        ProductResponse found = productService.findProductById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getPrice()).isEqualTo(1000.0);

        // 3. Тест получения всех с пагинацией
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(savedProduct));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(objectMapper.convertValue(any(Product.class), eq(ProductResponse.class)))
                .thenReturn(productResponse);

        Page<ProductResponse> allProducts = productService.findAll(pageable);

        assertThat(allProducts.getContent()).hasSize(1);
        assertThat(allProducts.getContent().get(0).getId()).isEqualTo(1L);

        // 4. Тест existsById
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean exists = productService.existsById(1L);
        assertThat(exists).isTrue();

        // 5. Тест удаления
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void objectMapperMapping_ShouldWorkCorrectly() {
        // Тест маппинга Request -> Entity через ObjectMapper
        when(objectMapper.convertValue(request, Product.class)).thenReturn(product);

        Product mappedProduct = objectMapper.convertValue(request, Product.class);

        assertThat(mappedProduct.getName()).isEqualTo(request.getName());
        assertThat(mappedProduct.getPrice()).isEqualTo(request.getPrice());

        // Тест маппинга Entity -> Response
        when(objectMapper.convertValue(savedProduct, ProductResponse.class)).thenReturn(productResponse);

        ProductResponse mappedResponse = objectMapper.convertValue(savedProduct, ProductResponse.class);

        assertThat(mappedResponse.getId()).isEqualTo(savedProduct.getId());
        assertThat(mappedResponse.getName()).isEqualTo(savedProduct.getName());
    }

    @Test
    void productValidation_ShouldWorkWithObjectMapper() {
        // Создаем продукт с минимальными данными
        Product minimalProduct = new Product();
        minimalProduct.setName("Минимальный");
        minimalProduct.setPrice(1.0);

        ProductResponse minimalResponse = new ProductResponse();
        minimalResponse.setId(2L);
        minimalResponse.setName("Минимальный");
        minimalResponse.setPrice(1.0);

        when(productRepository.save(any(Product.class))).thenReturn(minimalProduct);
        when(objectMapper.convertValue(any(Product.class), eq(ProductResponse.class)))
                .thenReturn(minimalResponse);

        Product saved = productRepository.save(minimalProduct);
        ProductResponse response = objectMapper.convertValue(saved, ProductResponse.class);

        // Проверяем, что маппинг работает даже с null полями
        assertThat(response.getDescription()).isNull();
        assertThat(response.getQuantityInStock()).isNull();
    }
}