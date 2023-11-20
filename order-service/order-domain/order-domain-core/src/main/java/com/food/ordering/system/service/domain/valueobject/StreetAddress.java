package com.food.ordering.system.service.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
public class StreetAddress {

    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;
}
