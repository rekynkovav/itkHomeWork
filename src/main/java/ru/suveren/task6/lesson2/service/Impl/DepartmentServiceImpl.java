package ru.suveren.task6.lesson2.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.suveren.task6.lesson2.model.Department;
import ru.suveren.task6.lesson2.repository.DepartmentRepository;
import ru.suveren.task6.lesson2.service.DepartmentService;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    @Override
    public Department save(Department department) {
        return repository.save(department);
    }

    @Override
    public Department get(Long id) {
        return repository.getById(id);
    }

    @Override
    public boolean update(Department department) {
       return repository.update(department);
    }

    @Override
    public void delete(Department department) {
        repository.delete(department);
    }
}
