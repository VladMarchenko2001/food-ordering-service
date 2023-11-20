package com.food.ordering.system.service.domain;

import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    OrderCreatedEvent initiate(Order order, Restaurant restaurant);

    OrderPaidEvent pay(Order order);

    void approve(Order order);

    OrderCancelledEvent cancelPayment(Order order, List<String> failureMessages);

    void cancel(Order order, List<String> failureMessages);
}
