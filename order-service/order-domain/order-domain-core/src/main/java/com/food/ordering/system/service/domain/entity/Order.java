package com.food.ordering.system.service.domain.entity;

import com.food.ordering.system.order.service.domain.dto.entity.AggregateRoot;
import com.food.ordering.system.order.service.domain.dto.valueobject.CustomerId;
import com.food.ordering.system.order.service.domain.dto.valueobject.Money;
import com.food.ordering.system.order.service.domain.dto.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.dto.valueobject.OrderStatus;
import com.food.ordering.system.order.service.domain.dto.valueobject.RestaurantId;
import com.food.ordering.system.service.domain.exception.OrderDomainException;
import com.food.ordering.system.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.service.domain.valueobject.TrackingId;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.food.ordering.system.order.service.domain.dto.valueobject.OrderStatus.APPROVED;
import static com.food.ordering.system.order.service.domain.dto.valueobject.OrderStatus.CANCELLED;
import static com.food.ordering.system.order.service.domain.dto.valueobject.OrderStatus.CANCELLING;
import static com.food.ordering.system.order.service.domain.dto.valueobject.OrderStatus.PAID;
import static com.food.ordering.system.order.service.domain.dto.valueobject.OrderStatus.PENDING;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.UUID.randomUUID;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Getter
public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.id);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public void initiateOrder() {
        setId(new OrderId(randomUUID()));
        this.trackingId = new TrackingId(randomUUID());
        this.orderStatus = PENDING;
        initializeOrderItems();

    }

    public Order validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemPrice();
        return this;
    }

    public Order pay() {
        if (!PENDING.equals(orderStatus)) throw new OrderDomainException("Order is not pending");
        this.orderStatus = PAID;
        return this;
    }

    public Order approve() {
        if (!PAID.equals(orderStatus)) throw new OrderDomainException("Order is not paid");
        this.orderStatus = APPROVED;
        return this;
    }

    public Order initCancelling(List<String> failureMessages) {
        if (!PAID.equals(orderStatus)) throw new OrderDomainException("Order is not paid");
        this.orderStatus = CANCELLING;
        updateFailureMessages(failureMessages);
        return this;
    }

    public Order cancel(List<String> failureMessages) {
        if (!CANCELLING.equals(orderStatus)) throw new OrderDomainException("Order is not cancelling");
        this.orderStatus = CANCELLED;
        updateFailureMessages(failureMessages);
        return this;
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem item: items) {
            item.initializeOrderItem(getId(), new OrderItemId(itemId++));
        }
    }

    private void validateInitialOrder() {
        if (nonNull(orderStatus) || nonNull(getId())) {
            throw new OrderDomainException("Order is already initialized");
        }
    }

    private void validateTotalPrice() {
        if (isNull(price) || !price.isGreaterThanZero())
            throw new OrderDomainException("Order price should be greater than zero");
    }

    private void validateItemPrice() {
        Money orderItemsTotal = items.stream()
                .map(item -> validateItemPrice(item).getSubTotal())
                .reduce(Money.ZERO_MONEY, Money::add);

        if (price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price +
                    " is not equal to Order items' prices: " + orderItemsTotal);
        }
    }

    private OrderItem validateItemPrice(OrderItem item) {
        if (item.isPriceValid()) return item;
        throw new OrderDomainException("Order item price: " + item.getPrice().getAmount() +
                    "is not valid for product: " + item.getProduct().getId().getValue());
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (isNull(this.failureMessages)) {
            this.failureMessages = failureMessages.stream()
                    .filter(StringUtils::isNotBlank)
                    .toList();
        }
        if (isNotEmpty(failureMessages)) {
            this.failureMessages.addAll(failureMessages.stream()
                    .filter(StringUtils::isNotBlank)
                    .toList());
        }
    }


    public static final class Builder {
        private OrderId id;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(OrderId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
