package ru.suveren.task5.lesson1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {

    @NotBlank(message = "Имя обязательно")
    private String name;

    @Size(min = 6, message = "Пароль минимум 6 символов")
    private String password;

    @Email(message = "Некорректный email")
    private String email;

}
