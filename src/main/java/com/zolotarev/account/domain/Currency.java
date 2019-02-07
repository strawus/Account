package com.zolotarev.account.domain;

import com.zolotarev.util.Contract;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.zolotarev.constant.Messages.*;
import static java.math.BigDecimal.TEN;

/**
 * Value object represents monetary currency and operations above it
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum Currency {
    RUB(2),
    USD(2),
    EUR(2);

    //Scale of the decimal part of the amount
    private final int scale;

    /**
     * Converts amount to minor currency by scale
     * @param amount Amount to convert. Must be not null, positive and its scale less than currency scale
     * @return Amount in minor currency. Not null
     */
    public BigInteger toMinorCurrency(BigDecimal amount) {
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresMoreOrEquals(amount, BigDecimal.ZERO, AMOUNT_MUST_BE_NOT_NEGATIVE);
        Contract.requiresMoreOrEquals(scale, amount.scale(), TOO_HIGH_SCALE_OF_THE_AMOUNT);

        return amount.multiply(TEN.pow(scale)).toBigIntegerExact();
    }

    /**
     * Converts amount to major currency by scale
     * @param amount Amount to convert. Must be not null and positive
     * @return Amount in minor currency. Not null
     */
    public BigDecimal toMajorCurrency(BigInteger amount) {
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresMoreOrEquals(amount, BigInteger.ZERO, AMOUNT_MUST_BE_POSITIVE);

        return new BigDecimal(amount, scale);
    }

    public static Currency parse(String currencyCode) {
        Contract.requiresNotNull(currencyCode, CURRENCY_CODE_MUST_BE_NOT_NULL);
        return Currency.valueOf(currencyCode.trim().toUpperCase());
    }
}
