package ru.suveren.task6.lesson2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suveren.task6.lesson2.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean update(Department department);
}
