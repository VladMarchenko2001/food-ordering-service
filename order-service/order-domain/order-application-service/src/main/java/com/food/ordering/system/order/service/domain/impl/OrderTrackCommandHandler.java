package com.food.ordering.system.order.service.domain.impl;

import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.port.output.repository.OrderRepository;
import com.food.ordering.system.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderDataMapper.toTrackOrderResponse(
                orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()))
                        .orElseThrow(() -> {
                            log.warn("Order not found with tracking id: {}", trackOrderQuery.getOrderTrackingId());
                            return new OrderNotFoundException("Order not found with tracking id: " + trackOrderQuery.getOrderTrackingId());
                        }));
    }
}
