package ru.suveren.task6.lesson2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.suveren.task6.lesson2.model.Employee;
import ru.suveren.task6.lesson2.model.EmployeeProjection;

import java.util.List;

public interface EmployeeProjectionRepository extends JpaRepository<Employee, Long> {
    List <EmployeeProjection> findByFirstNameAndLastNameAndPositionAndDepartment (Long id);
}
