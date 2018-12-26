package com.zolotarev.helper;

import com.zolotarev.account.domain.Account;
import com.zolotarev.account.repository.AccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.*;

/**
 * Prepares data for testing
 */
@Component
@RequiredArgsConstructor
public class TestDataHelper {

    private final @NonNull AccountRepository accountRepository;

    /**
     * Populates the database with data
     */
    public void prepareDatabaseData() {
        accountRepository.deleteAll();
        accountRepository.save(new Account(1, BigDecimal.valueOf(150L), RUB));
        accountRepository.save(new Account(2, BigDecimal.valueOf(250L), USD));
        accountRepository.save(new Account(3, BigDecimal.valueOf(350L), EUR));
        accountRepository.save(new Account(4, BigDecimal.valueOf(1500L), RUB));
        accountRepository.save(new Account(5, BigDecimal.valueOf(200015L, 2), USD));
        accountRepository.save(new Account(6, BigDecimal.valueOf(300L), EUR));
        accountRepository.save(new Account(7, BigDecimal.valueOf(15L), RUB));
        accountRepository.save(new Account(8, BigDecimal.valueOf(200L), USD));
        accountRepository.save(new Account(9, BigDecimal.valueOf(35L, 1), EUR));
        accountRepository.save(new Account(10, BigDecimal.valueOf(150L), RUB));
        accountRepository.save(new Account(11, BigDecimal.valueOf(250L), USD));
        accountRepository.save(new Account(12, BigDecimal.valueOf(350L), EUR));
        accountRepository.save(new Account(13, BigDecimal.valueOf(100L), RUB));
    }
}
