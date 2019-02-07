package com.zolotarev.account.repository;

import com.zolotarev.account.domain.Account;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.helper.TestDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test suite for {@link AccountRepository}
 */
@SpringBootTest
public class AccountRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private TestDataHelper dataHelper;

    @BeforeClass
    public void setUp() {
        dataHelper.prepareDatabaseData();
    }

    /**
     * Checking getting account by id from repository
     */
    @Test
    public void findByIdTest() {
        final Optional<Account> account = repository.findById(1);

        assertTrue(account.isPresent());
    }

    /**
     * Checking saving account to repository
     */
    @Test
    public void saveTest() {
        final Account account = repository.save(new Account(BigDecimal.TEN, Currency.RUB));

        final Optional<Account> savedAccount = repository.findById(account.getId());

        assertEquals(Optional.of(account), savedAccount);
    }

    /**
     * Checking getting all accounts from repository
     */
    @Test
    public void findAllTest() {
        final Iterable<Account> accounts = repository.findAll();

        assertTrue(accounts.iterator().hasNext());
    }

    /**
     * Checking deleting account by id from repository
     */
    @Test
    public void deleteTest() {
        final Account account = repository.save(new Account(Currency.EUR));

        repository.delete(account);

        final Optional<Account> deletedAccount = repository.findById(account.getId());

        assertFalse(deletedAccount.isPresent());
    }
}
