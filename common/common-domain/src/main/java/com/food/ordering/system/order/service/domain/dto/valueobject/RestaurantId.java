package com.food.ordering.system.order.service.domain.dto.valueobject;

import java.util.UUID;

public class RestaurantId extends BaseId<UUID> {

    public RestaurantId(UUID value) {
        super(value);
    }
}
