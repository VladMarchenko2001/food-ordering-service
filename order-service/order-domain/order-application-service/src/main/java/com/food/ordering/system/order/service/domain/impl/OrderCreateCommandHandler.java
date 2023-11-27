package com.food.ordering.system.order.service.domain.impl;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.port.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.port.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.port.output.repository.RestaurantRepository;
import com.food.ordering.system.service.domain.OrderDomainService;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.service.domain.exception.OrderDomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;
    private final ApplicationDomainEventPublisher applicationDomainEventPublisher;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.toOrder(createOrderCommand);
        OrderCreatedEvent createdEvent = orderDomainService.initiate(order, restaurant);
        Order saved = orderRepository.save(order);
        log.info("Order created with id: {}", saved.getId().getValue());
        applicationDomainEventPublisher.publish(createdEvent);

        return orderDataMapper.toCreateOrderResponse(saved, "Order created successfully");
    }

    private void checkCustomer(UUID customerId) {
        customerRepository.find(customerId).orElseThrow(() -> {
            log.warn("Customer not found with id: {}", customerId);
            return new OrderDomainException("Customer not found with id: " + customerId);
        });
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        return restaurantRepository.findRestaurantInfo(orderDataMapper.toRestaurant(createOrderCommand))
                .orElseThrow(() -> {
                    log.warn("Restaurant not found with id: {}", createOrderCommand.getRestaurantId());
                    return new OrderDomainException("Restaurant not found with id: " + createOrderCommand.getRestaurantId());
                });
    }
}
