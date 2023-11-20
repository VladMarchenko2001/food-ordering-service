package com.food.ordering.system.order.service.domain.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderAddressDto {

    // TODO: 18.11.2023 change jakarta.validation.constraints.NotNull to javax.validation.constraints.NotNull
    @NotNull
    @Max(50)
    private final String street;
    @NotNull
    @Max(10)
    private final String postalCode;
    @NotNull
    private final String city;
}
