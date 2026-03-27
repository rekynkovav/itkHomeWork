package ru.suveren.task6.lesson2.service;

import ru.suveren.task6.lesson2.model.Employee;

public interface EmployeeService {
    Employee save(Employee employee);
    Employee get(Long id);
    boolean update (Employee employee);
    void delete (Employee employee);
}
