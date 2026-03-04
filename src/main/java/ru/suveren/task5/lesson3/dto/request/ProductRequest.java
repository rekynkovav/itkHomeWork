package ru.suveren.task5.lesson3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 20, message = "имя от до 20 символов")
    private String name;

    @NotBlank(message = "цена обязательна")
    private Double price;
}
