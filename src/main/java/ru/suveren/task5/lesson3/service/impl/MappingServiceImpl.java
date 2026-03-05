package ru.suveren.task5.lesson3.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.suveren.task5.lesson3.dto.response.CustomerResponse;
import ru.suveren.task5.lesson3.model.Customer;
import ru.suveren.task5.lesson3.service.MappingService;

@Service
@RequiredArgsConstructor
public class MappingServiceImpl implements MappingService {

    private final ObjectMapper objectMapper;

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        return objectMapper.convertValue(source, targetType);
    }

    @Override
    public CustomerResponse toResponse(Customer customer) {
        return objectMapper.convertValue(customer, CustomerResponse.class);
    }
}
