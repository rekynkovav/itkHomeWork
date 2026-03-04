package ru.suveren.task5.lesson3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.suveren.task5.lesson3.model.Order;

public interface OrderRepository extends JpaRepository<Long,Order> {
}
