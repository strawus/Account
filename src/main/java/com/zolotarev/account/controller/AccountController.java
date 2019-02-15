package com.zolotarev.account.controller;

import com.zolotarev.account.controller.dto.AccountDto;
import com.zolotarev.account.controller.dto.AccountDto.CreateAccount;
import com.zolotarev.account.controller.dto.AccountDto.UpdateAccount;
import com.zolotarev.account.domain.Account;
import com.zolotarev.account.mapper.AccountMapper;
import com.zolotarev.account.service.AccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides an basic api for working with accounts by rest
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final @NonNull AccountService service;
    private final @NonNull AccountMapper mapper;

    /**
     * @param accountId Id took from request parameter
     * @return Found account
     */
    @GetMapping("/{accountId}")
    public AccountDto getById(@PathVariable Integer accountId) {
        final Account result = service.getById(accountId);
        return mapper.toDto(result);
    }

    /**
     * Creates account if it doesn't exist
     *
     * @param accountDto Entity to be created. Must be not null and its id must be null
     * @return Created entity with auto generated fields. Not null
     */
    @PostMapping
    public AccountDto create(@Validated(CreateAccount.class) @RequestBody AccountDto accountDto) {
        final Account account = mapper.toEntity(accountDto);
        final Account result = service.create(account);
        return mapper.toDto(result);
    }

    /**
     * Updates account if it already exist
     *
     * @param accountDto Entity to be updated. Must be not null and its id must be not null
     * @return Updated entity. Not null
     */
    @PutMapping
    public AccountDto update(@Validated(UpdateAccount.class) @RequestBody AccountDto accountDto) {
        final Account account = mapper.toEntity(accountDto);
        final Account result = service.update(account);
        return mapper.toDto(result);
    }

    /**
     * Deletes account by its id
     *
     * @param accountId Account id to delete. Mustn't be null
     */
    @DeleteMapping("/{accountId}")
    public void delete(@PathVariable Integer accountId) {
        service.delete(accountId);
    }

    /**
     * @return All accounts in a storage
     */
    @GetMapping
    public List<AccountDto> getAll() {
        final List<Account> result = service.getAll();
        return mapper.toDtoList(result);
    }
}
