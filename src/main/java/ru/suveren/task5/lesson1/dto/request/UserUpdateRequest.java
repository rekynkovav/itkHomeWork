package ru.suveren.task5.lesson1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @NotBlank(message = "Имя обязательно")
    private String name;

    @Email(message = "Некорректный email")
    private String email;
}
