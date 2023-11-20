package com.food.ordering.system.order.service.domain.port.output.message.publisher.payment;

import com.food.ordering.system.order.service.domain.dto.event.publisher.DomainEventPublisher;
import com.food.ordering.system.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {

}
