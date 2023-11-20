package com.food.ordering.system.order.service.domain.dto.create;

import com.food.ordering.system.order.service.domain.dto.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

    private final UUID orderTrackingId;
    private final OrderStatus orderStatus;
    private final String message;
}
