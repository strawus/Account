package com.zolotarev.account.service;

import com.zolotarev.account.domain.Account;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.exception.EntityNotFoundException;
import com.zolotarev.exception.InternalException;
import com.zolotarev.util.Contract;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.zolotarev.constant.Messages.*;
import static java.math.BigDecimal.ZERO;

/**
 * Used for managing account funds
 */
@Service
@RequiredArgsConstructor
public class ManageFundsService {

    private final @NonNull AccountService accountService;
    private final @NonNull CurrencyExchanger exchanger;

    /**
     * Deposit some amount to an account
     * @param accountId Account id to deposit. Mustn't be null
     * @param amount Amount to deposit. Mustn't be null
     * @param currency Currency of amount. Mustn't be null
     * @return Account entity after deposit. Not null
     * @throws EntityNotFoundException If entity not found by account id
     * @throws InternalException If some error occurs in a storage
     * @throws IllegalArgumentException If method parameters are invalid
     */
    public Account deposit(Integer accountId, BigDecimal amount, Currency currency) {
        Contract.requiresNotNull(accountId, ACCOUNT_ID_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresMore(amount, ZERO, AMOUNT_MUST_BE_POSITIVE);
        Contract.requiresNotNull(currency, CURRENCY_MUST_BE_NOT_NULL);

        final Account account = accountService.getById(accountId);

        amount = exchanger.exchange(amount, currency, account.getCurrency());
        Contract.requiresMore(amount, ZERO, TOO_SMALL_AMOUNT_TO_DEPOSIT);

        account.deposit(amount);

        return accountService.update(account);
    }

    /**
     * Withdraw some amount from an account
     * @param accountId Account id to withdraw. Mustn't be null
     * @param amount Amount to withdraw. Must be not null and positive
     * @param currency Currency of account. Mustn't be null
     * @return Account entity after withdraw. Not null
     * @throws EntityNotFoundException If entity not found by account id
     * @throws InternalException If some error occurs in a storage
     * @throws IllegalArgumentException If method parameters are invalid
     */
    public Account withdraw(Integer accountId, BigDecimal amount, Currency currency) {
        Contract.requiresNotNull(accountId, ACCOUNT_ID_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresMore(amount, ZERO, AMOUNT_MUST_BE_POSITIVE);
        Contract.requiresNotNull(currency, CURRENCY_MUST_BE_NOT_NULL);

        final Account account = accountService.getById(accountId);

        amount = exchanger.exchange(amount, currency, account.getCurrency());
        Contract.requiresMore(amount, ZERO, TOO_SMALL_AMOUNT_TO_WITHDRAW);

        account.withdraw(amount);

        return accountService.update(account);
    }

    /**
     * Transfers amount between accounts by their id. Ids mustn't match
     * @param fromAccountId Source account id. Must not be null
     * @param toAccountId Destination account id. Must be not null
     * @param amount Amount to transfer. Must be not null and positive
     * @param currency Currency of amount. Must not be null
     * @return Updated source account. Not null
     * @throws EntityNotFoundException If entity not found by account id
     * @throws InternalException If some error occurs in a storage
     * @throws IllegalArgumentException If method parameters are invalid
     */
    public Account transfer(Integer fromAccountId, Integer toAccountId, BigDecimal amount, Currency currency) {
        Contract.requiresNotNull(fromAccountId, SOURCE_ACCOUNT_ID_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(toAccountId, TARGET_ACCOUNT_ID_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(amount, AMOUNT_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(currency, CURRENCY_MUST_BE_NOT_NULL);
        Contract.requiresNotEquals(fromAccountId, toAccountId, UNABLE_TO_TRANSFER_TO_THE_SAME_ACCOUNT);
        Contract.requiresMore(amount, ZERO, AMOUNT_MUST_BE_POSITIVE);

        final Account fromAccount = accountService.getById(fromAccountId);
        final Account toAccount = accountService.getById(toAccountId);

        final BigDecimal fromAmount = exchanger.exchange(amount, currency, fromAccount.getCurrency());
        Contract.requiresMore(fromAmount, ZERO, TOO_SMALL_AMOUNT_TO_TRANSFER);
        fromAccount.withdraw(fromAmount);

        final BigDecimal toAmount = exchanger.exchange(amount, currency, toAccount.getCurrency());
        Contract.requiresMore(toAmount, ZERO, TOO_SMALL_AMOUNT_TO_TRANSFER);
        toAccount.deposit(toAmount);

        //Order updating to avoid deadlocks
        final Account first = fromAccountId > toAccountId ? fromAccount : toAccount;
        final Account second = fromAccountId > toAccountId ? toAccount : fromAccount;
        accountService.update(first);
        accountService.update(second);

        return fromAccount;
    }
}
