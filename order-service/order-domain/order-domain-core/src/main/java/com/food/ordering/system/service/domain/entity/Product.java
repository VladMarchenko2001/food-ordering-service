package com.food.ordering.system.service.domain.entity;

import com.food.ordering.system.order.service.domain.dto.entity.BaseEntity;
import com.food.ordering.system.order.service.domain.dto.valueobject.Money;
import com.food.ordering.system.order.service.domain.dto.valueobject.ProductId;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Product extends BaseEntity<ProductId> {

    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        super.setId(productId);
    }

    public Product updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
        return this;
    }
}
