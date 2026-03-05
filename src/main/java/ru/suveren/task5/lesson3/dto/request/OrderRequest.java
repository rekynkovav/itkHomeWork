package ru.suveren.task5.lesson3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

    @NotBlank(message = "Адрес доставки обязателен")
    private String shippingAddress;

    @NotBlank(message = "Общая стоимость заказа обязательна")
    @Size(min = 1)
    private Double totalPrice;
}
