package ru.suveren.task5.lesson3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Long, CustomerRepository> {
}
