package ru.suveren.task5.lesson3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suveren.task5.lesson3.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
