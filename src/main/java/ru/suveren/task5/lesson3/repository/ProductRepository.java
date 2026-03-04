package ru.suveren.task5.lesson3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.suveren.task5.lesson3.model.Product;

public interface ProductRepository extends JpaRepository<Long, Product> {
}
