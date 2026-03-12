package ru.suveren.task5.lesson3.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Введите корректный email-адрес")
    private String email;

    @NotBlank(message = "")
    @Size (min = 11, max = 12)
    private String contactNumber;
}
