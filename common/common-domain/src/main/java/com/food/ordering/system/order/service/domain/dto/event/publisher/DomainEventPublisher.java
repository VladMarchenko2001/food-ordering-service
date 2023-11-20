package com.food.ordering.system.order.service.domain.dto.event.publisher;

import com.food.ordering.system.order.service.domain.dto.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T event);
}
