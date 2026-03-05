package ru.suveren.task5.lesson2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateRequest {

    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Некорректный title")
    private String title;

}
