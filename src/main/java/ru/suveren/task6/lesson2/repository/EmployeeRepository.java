package ru.suveren.task6.lesson2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suveren.task6.lesson2.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean update(Employee employee);
}
