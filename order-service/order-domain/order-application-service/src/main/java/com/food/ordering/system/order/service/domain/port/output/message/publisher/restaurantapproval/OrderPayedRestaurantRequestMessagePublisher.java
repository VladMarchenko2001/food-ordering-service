package com.food.ordering.system.order.service.domain.port.output.message.publisher.restaurantapproval;

import com.food.ordering.system.order.service.domain.dto.event.publisher.DomainEventPublisher;
import com.food.ordering.system.service.domain.event.OrderPaidEvent;

public interface OrderPayedRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
