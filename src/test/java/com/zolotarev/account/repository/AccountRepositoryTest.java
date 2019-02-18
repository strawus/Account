package com.zolotarev.account.repository;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import com.zolotarev.account.AccountConfiguration;
import com.zolotarev.account.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static com.zolotarev.account.domain.Currency.RUB;
import static java.math.BigDecimal.TEN;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.testng.Assert.*;

/**
 * Test class for {@link AccountRepository}
 */
@ActiveProfiles("dev,embeddedPostgres")
@AutoConfigureTestDatabase(replace = NONE)
@EntityScan("com.zolotarev.account.domain")
@ContextConfiguration(classes = AccountConfiguration.class)
@DataJpaTest(properties = "spring.jpa.properties.hibernate.cache.use_second_level_cache=false")
public class AccountRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AccountRepository repository;

    private Account testAccount;

    @BeforeMethod
    public void init() {
        SQLStatementCountValidator.reset();
    }

    /**
     * Checking saving account to repository
     */
    @Test(priority = -1)
    public void insertTest() {
        testAccount = repository.save(new Account(TEN, RUB));

        assertNotNull(testAccount.getId());

        assertInsertCount(1);
    }

    /**
     * Checking getting account by id from repository
     */
    @Test
    public void findByIdTest() {
        final Optional<Account> account = repository.findById(testAccount.getId());

        assertEquals(account, Optional.of(testAccount));

        assertSelectCount(1);
    }

    /**
     * Checking getting all accounts from repository
     */
    @Test
    public void findAllTest() {
        final List<Account> accounts = repository.findAll();

        assertTrue(accounts.contains(testAccount));

        assertSelectCount(1);
    }

    /**
     * Checking deleting account by id from repository
     */
    @Test(priority = 1)
    public void deleteTest() {
        repository.deleteById(testAccount.getId());

        assertSelectCount(1);
        assertDeleteCount(1);
    }

}
