package ru.suveren.task5.lesson3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suveren.task5.lesson3.dto.request.OrderRequest;
import ru.suveren.task5.lesson3.service.impl.OrderServiceImpl;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping
    public ResponseEntity<?> getAllCustomer(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(orderService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> creatCustomer(@Valid @RequestBody OrderRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("некорректные данные");
        } else {
            try {
                orderService.save(request);
                return ResponseEntity.ok("покупатель сохранен");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ошибка на стороне сервера");
            }
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCustomerById(@PathVariable Long id) {
        try {
            if (orderService.existsById(id)) {
                orderService.deleteById(id);
                return ResponseEntity.ok("Книга удалена");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Книга не найдена");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
