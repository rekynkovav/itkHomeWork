package ru.suveren.task5.lesson3.service;

import ru.suveren.task5.lesson3.dto.response.CustomerResponse;
import ru.suveren.task5.lesson3.model.Customer;

public interface MappingService {

    <T> T convert(Object source, Class<T> targetType);
    CustomerResponse toResponse(Customer customer);
}
