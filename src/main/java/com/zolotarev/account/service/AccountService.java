package com.zolotarev.account.service;

import com.zolotarev.account.mapper.CopyMachine;
import com.zolotarev.account.domain.Account;
import com.zolotarev.exception.EntityNotFoundException;
import com.zolotarev.exception.InternalException;
import com.zolotarev.account.repository.AccountRepository;
import com.zolotarev.util.Contract;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zolotarev.constant.Messages.*;

/**
 * Used to perform basic operations on the account
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {


    private final @NonNull AccountRepository repository;
    private final @NonNull CopyMachine copyMachine;

    /**
     * @param accountId Account id to find it. Must be not null
     * @return Found account. Not null
     * @throws InternalException        If some error occurs in a storage
     * @throws EntityNotFoundException  If an entity not found in a storage
     * @throws IllegalArgumentException If method parameters are invalid
     */
    public Account getById(Integer accountId) {
        Contract.requiresNotNull(accountId, ACCOUNT_ID_MUST_BE_NOT_NULL);

        Optional<Account> account;
        try {
            account = repository.findById(accountId);
        } catch (RuntimeException e) {
            log.error("Error getting account by id: {}. Reason[{}]: {}", accountId, e.getClass(), e.getMessage());
            throw new InternalException("Error getting account by id = " + accountId, e);
        }
        return account.orElseThrow(() -> new EntityNotFoundException(ACCOUNT_NOT_FOUND));
    }

    /**
     * Creates account if it doesn't exist
     *
     * @param account Entity to be created. Must be not null and its id must be null
     * @return Created entity with auto generated fields. Not null
     * @throws InternalException        If some error occurs in a storage
     * @throws IllegalArgumentException If method parameters are invalid
     */
    public Account create(Account account) {
        Contract.requiresNotNull(account, ACCOUNT_MUST_BE_NOT_NULL);
        Contract.requiresNull(account.getId(), ACCOUNT_ALREADY_EXIST);

        try {
            //Copy-on-update to avoid synchronization problems with thread unsafe entity
            return repository.save(copyMachine.copy(account));
        } catch (RuntimeException e) {
            log.error("Error creating account: {}. Reason[{}]: {}", account, e.getClass(), e.getMessage());
            throw new InternalException("Error creating account[" + account + "]", e);
        }
    }

    /**
     * Updates account if it already exist
     *
     * @param account Account to update. Must be not null and its id must be not null
     * @return Updated account. Not null
     * @throws InternalException        If some error occurs in a storage
     * @throws IllegalArgumentException If method parameters are invalid
     */
    public Account update(Account account) {
        Contract.requiresNotNull(account, ACCOUNT_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(account.getId(), ACCOUNT_IS_NOT_EXISTS);

        try {
            //Copy-on-update to avoid synchronization problems with thread unsafe entity
            return repository.save(copyMachine.copy(account));
        } catch (RuntimeException e) {
            log.error("Error updating account: {}. Reason[{}]: {}", account, e.getClass(), e.getMessage());
            throw new InternalException("Error updating account[" + account + "]", e);
        }
    }

    /**
     * Deletes account by its id
     *
     * @param accountId Account id to delete. Mustn't be null
     * @throws InternalException If some error occurs in a storage
     */
    public void delete(Integer accountId) {
        Contract.requiresNotNull(accountId, ACCOUNT_ID_MUST_BE_NOT_NULL);

        try {
            repository.deleteById(accountId);
        } catch (RuntimeException e) {
            log.error("Error deleting account by id: {}. Reason[{}]: {}", accountId, e.getClass(), e.getMessage());
            throw new InternalException("Error deleting account by id = " + accountId, e);
        }
    }

    /**
     * @return All accounts in a storage
     * @throws InternalException If any error occurs in a database
     */
    public List<Account> getAll() {
        try {
            final Iterable<Account> accountIterable = repository.findAll();
            //Some of implementations CrudRepository#findAll() returns List like Iterable. Casting it to List faster than iteration
            if (accountIterable instanceof List) {
                return (List<Account>) accountIterable;
            } else {
                final List<Account> accounts = new ArrayList<>();
                accountIterable.forEach(accounts::add);
                return accounts;
            }
        } catch (RuntimeException e) {
            log.error("Error getting all accounts. Reason[{}]: {}", e.getClass(), e.getMessage());
            throw new InternalException("Error getting all accounts", e);
        }
    }
}
