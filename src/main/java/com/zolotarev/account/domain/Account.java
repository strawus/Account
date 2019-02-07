package com.zolotarev.account.domain;

import com.zolotarev.util.Contract;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.zolotarev.constant.Messages.*;
import static java.math.BigDecimal.ZERO;

/**
 * Entity represents basic account operations
 */
@KeySpace("accounts")
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Account {

    //Unique account identificator. If this account didn't stored yet, it can't be used in HashSet/HashMap, because id haven't generated
    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    //Amount held in an account, converted to minor currency
    @Getter(AccessLevel.NONE)
    private BigInteger amountInMinorCurrency;
    //Currency in which the amount is
    private final Currency currency;

    public Account(Integer id, BigDecimal amount, Currency currency) {
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(currency, CURRENCY_MUST_BE_NOT_NULL);
        Contract.requiresMoreOrEquals(amount, ZERO, AMOUNT_MUST_BE_NOT_NEGATIVE);

        this.id = id;
        this.amountInMinorCurrency = currency.toMinorCurrency(amount);
        this.currency = currency;
    }

    public Account(BigDecimal amount, Currency currency) {
        this(null, amount, currency);
    }

    public Account(Currency currency) {
        this(ZERO, currency);
    }

    /**
     * Deposit some amount to an account
     *
     * @param amount Amount to deposit. Must be not null and positive
     */
    public void deposit(BigDecimal amount) {
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresMore(amount, ZERO, AMOUNT_MUST_BE_POSITIVE);

        amountInMinorCurrency = amountInMinorCurrency.add(currency.toMinorCurrency(amount));
    }

    /**
     * Withdraw some amount from the account
     *
     * @param amount Amount to withdraw. Must be not null, positive and less or equals than amount on the account
     */
    public void withdraw(BigDecimal amount) {
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresMore(amount, ZERO, AMOUNT_MUST_BE_POSITIVE);

        final BigInteger withdrawAmount = currency.toMinorCurrency(amount);
        Contract.requiresMoreOrEquals(amountInMinorCurrency, withdrawAmount, NOT_ENOUGH_MONEY_TO_WITHDRAW);

        amountInMinorCurrency = amountInMinorCurrency.subtract(withdrawAmount);
    }

    /**
     * @return Amount in major currency
     */
    public BigDecimal getAmount() {
        return currency.toMajorCurrency(amountInMinorCurrency);
    }
}

