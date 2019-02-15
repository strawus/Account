package com.zolotarev.account.domain;

import com.zolotarev.util.Contract;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.zolotarev.constant.Messages.*;
import static java.math.BigDecimal.ZERO;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PRIVATE;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

/**
 * Entity represents basic account operations
 */
@Entity
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = PRIVATE)
@Cache(usage = READ_WRITE, region = "accounts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Account implements Serializable {

    //Unique account identificator. If this account didn't stored yet, it can't be used in HashSet/HashMap, because id haven't generated
    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "account_id_seq", allocationSize = 10)
    @GeneratedValue(strategy = SEQUENCE, generator = "account_id_seq")
    private Integer id;

    //Amount held in an account, converted to minor currency
    @Getter(AccessLevel.NONE)
    private BigInteger amountInMinorCurrency;

    //Currency in which the amount is
    @Enumerated(STRING)
    @Column(name = "currency_code", nullable = false)
    private Currency currency;

    //Entity version. Uses for optimistic updates in database
    @Version
    private Integer version;

    public Account(Integer id, BigDecimal amount, Currency currency, Integer version) {
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(currency, CURRENCY_MUST_BE_NOT_NULL);
        Contract.requiresMoreOrEquals(amount, ZERO, AMOUNT_MUST_BE_NOT_NEGATIVE);

        this.id = id;
        this.amountInMinorCurrency = currency.toMinorCurrency(amount);
        this.currency = currency;
        this.version = version;
    }

    public Account(Integer id, BigDecimal amount, Currency currency) {
        this(id, amount, currency, null);
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

