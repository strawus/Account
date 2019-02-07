package com.zolotarev.account.repository;

import com.zolotarev.account.domain.Account;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

/**
 * Database abstraction layer for an account entity
 */
@Repository
public interface AccountRepository extends KeyValueRepository<Account, Integer> { }
