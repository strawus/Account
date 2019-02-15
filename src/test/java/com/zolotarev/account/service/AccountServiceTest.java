package com.zolotarev.account.service;


import com.zolotarev.account.domain.Account;
import com.zolotarev.account.repository.AccountRepository;
import com.zolotarev.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.zolotarev.account.domain.Currency.*;
import static java.math.BigDecimal.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Test class for {@link AccountService}
 */
@SpringBootTest(classes = AccountService.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class AccountServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AccountService service;
    @MockBean
    private AccountRepository repository;

    @DataProvider
    public static Object[][] createAccountData() {
        return new Object[][]{
                {new Account(ZERO, RUB), new Account(1, ZERO, RUB)},
                {new Account(ONE, USD), new Account(2, ONE, USD)},
                {new Account(BigDecimal.valueOf(10000L), EUR), new Account(3, BigDecimal.valueOf(10000L), EUR)}
        };
    }

    /**
     * Checking that account created by prepared data
     */
    @Test(dataProvider = "createAccountData")
    public void createAccountTest(final Account testAccount, final Account expectedAccount) {
        when(repository.save(testAccount)).thenReturn(expectedAccount);
        final Account savedAccount = service.create(testAccount);

        assertEquals(savedAccount, expectedAccount);
    }

    @DataProvider
    public static Object[][] updateAccountData() {
        return new Object[][]{
                {new Account(4, BigDecimal.valueOf(100L), RUB)},
                {new Account(5, ZERO, USD)},
                {new Account(6, BigDecimal.valueOf(10000L), EUR)}
        };
    }

    /**
     * Checking that account updated by prepared data
     */
    @Test(dataProvider = "updateAccountData")
    public void updateAccountTest(Account testAccount) {
        when(repository.save(testAccount)).thenReturn(testAccount);

        final Account updatedAccount = service.update(testAccount);

        assertEquals(updatedAccount, testAccount);
    }

    @DataProvider
    public static Object[][] getAccountData() {
        return new Object[][]{
                {7, new Account(7, ZERO, RUB)},
                {8, new Account(8, ONE, EUR)},
                {9, new Account(9, TEN, USD)}
        };
    }

    /**
     * Checking that getting account by id does works
     */
    @Test(dataProvider = "getAccountData")
    public void getAccountTest(Integer accountId, Account expectedAccount) {
        when(repository.findById(accountId)).thenReturn(Optional.of(expectedAccount));

        final Account account = service.getById(accountId);

        assertEquals(account, expectedAccount);
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
        doNothing().when(repository).deleteById(accountId);
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        service.delete(accountId);
        service.getById(accountId);
    }

    /**
     * Checking that list of accounts isn't empty
     */
    @Test
    public void getAllAccountsTest() {
        when(repository.findAll()).thenReturn(asList(new Account(RUB)));

        List<Account> accounts = service.getAll();
        assertFalse(accounts.isEmpty());
    }
}
