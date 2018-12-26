package com.zolotarev.account.service;


import com.zolotarev.account.domain.Account;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.exception.EntityNotFoundException;
import com.zolotarev.helper.TestDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.zolotarev.account.domain.Currency.RUB;
import static org.testng.Assert.*;

/**
 * Test suite for {@link AccountService}
 */
@SpringBootTest
public class AccountServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TestDataHelper testDataHelper;

    @BeforeClass
    public void setUp() {
        testDataHelper.prepareDatabaseData();
    }

    @DataProvider
    public static Object[][] createAccountData() {
        return new Object[][]{
                {BigDecimal.ZERO, RUB},
                {BigDecimal.ONE, Currency.USD},
                {BigDecimal.valueOf(10000L), Currency.EUR}
        };
    }

    /**
     * Checking that account created by prepared data
     */
    @Test(dataProvider = "createAccountData")
    public void createAccountTest(BigDecimal amount, Currency currency) {
        final Account account = accountService.create(new Account(amount, currency));

        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(account.getCurrency(), currency);
        assertTrue(account.getAmount().compareTo(amount) == 0);
    }

    @DataProvider
    public static Object[][] updateAccountData() {
        return new Object[][]{
                {new Account(10, BigDecimal.valueOf(100L), RUB)},
                {new Account(11, BigDecimal.ZERO, Currency.USD)},
                {new Account(12, BigDecimal.valueOf(10000L), Currency.EUR)}
        };
    }

    /**
     * Checking that account updated by prepared data
     */
    @Test(dataProvider = "updateAccountData")
    public void updateAccountTest(Account account) {
        final Account updatedAccount = accountService.update(account);

        assertNotNull(updatedAccount);
        assertEquals(updatedAccount.getCurrency(), account.getCurrency());
        assertTrue(updatedAccount.getAmount().compareTo(account.getAmount()) == 0);
    }

    @DataProvider
    public static Object[][] getAccountData() {
        return new Object[][]{
                {2},
                {1},
                {3}
        };
    }

    /**
     * Checking that getting account by id does works
     */
    @Test(dataProvider = "getAccountData")
    public void getAccountTest(Integer accountId) {
        final Account account = accountService.getById(accountId);

        assertNotNull(account);
        assertEquals(account.getId(), accountId);
    }

    @DataProvider
    public static Object[][] deleteAccountData() {
        return new Object[][]{
                {7},
                {8},
                {9},
        };
    }

    /**
     * Checking that entity won't exist after deleting
     */
    @Test(dataProvider = "deleteAccountData", expectedExceptions = EntityNotFoundException.class)
    public void deleteAccountTest(Integer accountId) {
        accountService.delete(accountId);
        accountService.getById(accountId);
    }

    /**
     * Checking that list of accounts isn't empty
     */
    @Test
    public void getAllAccountsTest() {
        List<Account> accounts = accountService.getAll();
        assertFalse(accounts.isEmpty());
    }
}
