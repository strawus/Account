package com.zolotarev.account.repository;

import com.zolotarev.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Database abstraction layer for an account entity
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> { }
