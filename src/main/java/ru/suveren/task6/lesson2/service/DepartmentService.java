package ru.suveren.task6.lesson2.service;

import ru.suveren.task6.lesson2.model.Department;

public interface DepartmentService {
    Department save(Department department);
    Department get(Long id);
    boolean update (Department department);
    void delete (Department department);
}
