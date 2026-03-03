package ru.suveren.task5.lesson2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateRequest {

    @NotBlank(message = "title обязателен")
    private String title;
}
