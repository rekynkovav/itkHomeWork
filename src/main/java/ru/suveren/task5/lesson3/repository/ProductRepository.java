package ru.suveren.task5.lesson3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suveren.task5.lesson3.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
