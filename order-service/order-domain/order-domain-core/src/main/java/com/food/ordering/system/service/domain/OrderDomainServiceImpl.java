package com.food.ordering.system.service.domain;

import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Product;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.service.domain.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent initiate(Order order, Restaurant restaurant) {
        validate(restaurant);
        setOrderProductInfo(order, restaurant);
        order.validateOrder().initiateOrder();
        log.info("Order with id {} is initiated", order.getId());
        return new OrderCreatedEvent(order, now(UTC));
    }

    @Override
    public OrderPaidEvent pay(Order order) {
        order.pay();
        log.info("Order with id {} is paid", order.getId());
        return new OrderPaidEvent(order, now(UTC));
    }

    @Override
    public void approve(Order order) {
        order.approve();
        log.info("Order with id {} is approved", order.getId());
    }

    @Override
    public OrderCancelledEvent cancelPayment(Order order, List<String> failureMessages) {
        order.initCancelling(failureMessages);
        log.info("Order with id {} is cancelling", order.getId());
        return new OrderCancelledEvent(order, now(UTC));
    }

    @Override
    public void cancel(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id {} is cancelled", order.getId());
    }

    private void validate(Restaurant restaurant) {
        if (!restaurant.isActive()) throw new IllegalArgumentException("Restaurant with id " + restaurant.getId() + " is not active");
    }

    private void setOrderProductInfo(Order order, Restaurant restaurant) {
        order.getItems().forEach(item -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = item.getProduct();
            if (currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        }));
    }
}
