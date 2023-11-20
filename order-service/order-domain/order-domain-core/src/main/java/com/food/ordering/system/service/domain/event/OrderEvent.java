package com.food.ordering.system.service.domain.event;

import com.food.ordering.system.order.service.domain.dto.event.DomainEvent;
import com.food.ordering.system.service.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;
}
