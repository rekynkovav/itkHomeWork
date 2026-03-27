package ru.suveren.task6.lesson2.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.suveren.task6.lesson2.model.Employee;
import ru.suveren.task6.lesson2.repository.EmployeeRepository;
import ru.suveren.task6.lesson2.service.EmployeeService;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    @Override
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Employee get(Long id) {
        return repository.getById(id);
    }

    @Override
    public boolean update(Employee employee) {
        return repository.update(employee);
    }

    @Override
    public void delete(Employee employee) {
        repository.delete(employee);
    }
}
