package com.zolotarev.account.controller;

import com.zolotarev.account.controller.dto.AccountDto;
import com.zolotarev.account.controller.dto.ManageFundsDto;
import com.zolotarev.account.controller.dto.TransferDto;
import com.zolotarev.account.mapper.AccountMapper;
import com.zolotarev.account.domain.Account;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.account.service.ManageFundsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Provides an api for managing funds by rest
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ManageFundsController {

    private final @NonNull ManageFundsService service;
    private final @NonNull AccountMapper mapper;

    /**
     * Deposit some amount to an account
     * @param manageFundsDto Contains information for deposit
     * @return Account after deposit
     */
    @PostMapping("/deposits")
    public AccountDto deposit(@Validated @RequestBody ManageFundsDto manageFundsDto) {
        final Currency currency = Currency.parse(manageFundsDto.getCurrencyCode());
        final Account result = service.deposit(manageFundsDto.getAccountId(), manageFundsDto.getAmount(), currency);
        return mapper.toDto(result);
    }

    /**
     * Withdraw some amount from an account
     * @param manageFundsDto Contains information for withdraw
     * @return Account after withdraw
     */
    @PostMapping("/withdraws")
    public AccountDto withdraw(@Validated @RequestBody ManageFundsDto manageFundsDto) {
        final Currency currency = Currency.parse(manageFundsDto.getCurrencyCode());
        final Account result = service.withdraw(manageFundsDto.getAccountId(), manageFundsDto.getAmount(), currency);
        return mapper.toDto(result);
    }

    /**
     * Transfers amount between accounts by their id. Ids mustn't match
     * @param transferDto Contains information for transfer
     * @return Source account after transfer
     */
    @PostMapping("/transfers")
    public AccountDto transfer(@Validated @RequestBody TransferDto transferDto) {
        final Integer fromAccountId = transferDto.getSourceAccountId();
        final Integer toAccountId = transferDto.getTargetAccountId();
        final BigDecimal amount = transferDto.getAmount();
        final Currency currency = Currency.parse(transferDto.getCurrencyCode());
        final Account result = service.transfer(fromAccountId, toAccountId, amount, currency);
        return mapper.toDto(result);
    }
}
