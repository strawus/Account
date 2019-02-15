package com.zolotarev.account.service;

import com.zolotarev.account.client.CurrencyRateClient;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.exception.InternalException;
import com.zolotarev.util.Contract;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.zolotarev.constant.Messages.*;
import static java.math.BigDecimal.ZERO;

/**
 * Contains methods for exchanging currencies
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyExchanger {

    private @NonNull CurrencyRateClient client;

    /**
     * Exchanges amount from one currency to another currency
     * @param amount  Amount to exchange. Must be not null and more than zero
     * @param current Current currency of amount. Must be not null
     * @param target  Target currency for amount. Must be not null
     * @return If current == target, then amount will return without changes.
     * In other case, amount transforms by multiplying to currency rate and rounding by {@link RoundingMode#HALF_EVEN} method
     * @throws InternalException        If some error occurs in a client
     * @throws IllegalArgumentException If method parameters are invalid
     */
    public BigDecimal exchange(BigDecimal amount, Currency current, Currency target) {
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(current, SOURCE_CURRENCY_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(target, TARGET_CURRENCY_MUST_BE_NOT_NULL);
        Contract.requiresMore(amount, ZERO, AMOUNT_MUST_BE_POSITIVE);

        if (current == target) return amount;

        return client.getRate(current, target)
                .map(amount::multiply)
                .map(exchangedAmount -> exchangedAmount.setScale(target.getScale(), RoundingMode.HALF_EVEN))
                .doOnError(e -> log.error("Error exchange amount {} from {} to {}. Reason[{}]: {}", amount, current, target, e.getClass(), e.getMessage()))
                .onErrorMap(e -> new InternalException("Cannot exchange amount " + amount + " from " + current + " to " + target, e))
                .block();
    }
}
