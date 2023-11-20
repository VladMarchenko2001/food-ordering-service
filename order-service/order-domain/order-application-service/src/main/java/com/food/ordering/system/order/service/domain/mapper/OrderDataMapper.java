package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddressDto;
import com.food.ordering.system.order.service.domain.dto.valueobject.CustomerId;
import com.food.ordering.system.order.service.domain.dto.valueobject.Money;
import com.food.ordering.system.order.service.domain.dto.valueobject.ProductId;
import com.food.ordering.system.order.service.domain.dto.valueobject.RestaurantId;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.OrderItem;
import com.food.ordering.system.service.domain.entity.Product;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {

    public Restaurant toRestaurant(CreateOrderCommand command) {
        return Restaurant.Builder.builder()
                .id(new RestaurantId(command.getRestaurantId()))
                .products(command.getItems().stream()
                        .map(item -> new Product(new ProductId(item.getProductId()))).toList())
                .build();
    }

    public Order toOrder(CreateOrderCommand command) {
        return Order.Builder.builder()
                .restaurantId(new RestaurantId(command.getRestaurantId()))
                .customerId(new CustomerId(command.getCustomerId()))
                .deliveryAddress(toStreetAddress(command.getAddress()))
                .price(new Money(command.getPrice()))
                .items(toOrderItems(command.getItems()))
                .build();
    }

    public CreateOrderResponse toCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private List<OrderItem> toOrderItems(List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> items) {
        return items.stream().map(this::toOrderItem).toList();
    }

    private OrderItem toOrderItem(com.food.ordering.system.order.service.domain.dto.create.OrderItem item) {
        return OrderItem.Builder.builder()
                .product(new Product(new ProductId(item.getProductId())))
                .price(new Money(item.getPrice()))
                .quantity(item.getQuantity())
                .subTotal(new Money(item.getSubTotal()))
                .build();
    }


    private StreetAddress toStreetAddress(OrderAddressDto address) {
        return new StreetAddress(UUID.randomUUID(), address.getStreet(), address.getPostalCode(), address.getCity());
    }
}
