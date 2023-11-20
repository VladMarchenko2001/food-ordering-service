package com.food.ordering.system.order.service.domain.dto.valueobject;

import com.food.ordering.system.order.service.domain.dto.constant.CommonConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

import static com.food.ordering.system.order.service.domain.dto.constant.CommonConstants.TWO;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Objects.nonNull;

@Getter
@EqualsAndHashCode
public class Money {

    public static final Money ZERO_MONEY = new Money(ZERO);

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isGreaterThanZero() {
        return nonNull(this.amount) && amount.compareTo(ZERO) > CommonConstants.ZERO;
    }

    public boolean isGreaterThan(Money money) {
        return nonNull(this.amount) && amount.compareTo(money.amount) > CommonConstants.ZERO;
    }

    public Money add(Money money) {
        return new Money(setScale(this.amount.add(money.getAmount())));
    }

    public Money subtract(Money money) {
        return new Money(setScale(this.amount.subtract(money.getAmount())));
    }

    public Money multiply(int multiplier) {
        return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }

    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(TWO, HALF_EVEN);
    }
}
