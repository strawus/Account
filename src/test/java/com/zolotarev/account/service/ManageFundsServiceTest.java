package com.zolotarev.account.service;

import com.zolotarev.account.client.CurrencyRateClient;
import com.zolotarev.account.domain.Account;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.helper.TestDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Test suite for {@link ManageFundsService}
 */
@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class ManageFundsServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ManageFundsService manageFundsService;
    @Autowired
    private TestDataHelper testDataHelper;
    @MockBean
    private CurrencyRateClient rateClient;

    @BeforeClass
    public void setUp() {
        testDataHelper.prepareDatabaseData();
        when(rateClient.getRate(any(), any())).thenReturn(Mono.just(ONE));
    }

    @DataProvider
    public static Object[][] depositAccountData() {
        return new Object[][]{
                {1, ONE, RUB},
                {2, TEN, USD},
                {3, BigDecimal.valueOf(10000L), EUR},
        };
    }

    /**
     * Checking that amount increase after deposit
     */
    @Test(dataProvider = "depositAccountData")
    public void depositAccountTest(Integer accountId, BigDecimal amount, Currency currency) {
        final Account accountBeforeDebit = accountService.getById(accountId);
        final BigDecimal expectedAmount = accountBeforeDebit.getAmount().add(amount);

        manageFundsService.deposit(accountId, amount, currency);

        final Account accountAfterDebit = accountService.getById(accountId);

        assertTrue(accountAfterDebit.getAmount().compareTo(expectedAmount) == 0);
    }

    @DataProvider
    public static Object[][] withdrawAccountData() {
        return new Object[][]{
                {4, ONE, RUB},
                {5, TEN, EUR},
                {6, BigDecimal.valueOf(250L), USD},
        };
    }

    /**
     * Checking that amount decrease after withdraw
     */
    @Test(dataProvider = "withdrawAccountData")
    public void withdrawAccountTest(Integer accountId, BigDecimal amount, Currency currency) {
        final Account accountBeforeCredit = accountService.getById(accountId);
        final BigDecimal expectedAmount = accountBeforeCredit.getAmount().subtract(amount);

        manageFundsService.withdraw(accountId, amount, currency);

        final Account accountAfterCredit = accountService.getById(accountId);

        assertTrue(accountAfterCredit.getAmount().compareTo(expectedAmount) == 0);
    }

    @DataProvider
    public static Object[][] transferData() {
        return new Object[][]{
                {1, 2, BigDecimal.valueOf(100L), RUB},
                {2, 1, BigDecimal.valueOf(200L), EUR},
                {3, 4, BigDecimal.valueOf(350L), USD}
        };
    }

    /**
     * Checking that amount changed after transfer
     */
    @Test(dataProvider = "transferData")
    public void transferTest(Integer fromAccountId, Integer toAccountId, BigDecimal amount, Currency currency) {
        final Account fromAccountBeforeTransfer = accountService.getById(fromAccountId);
        final Account toAccountBeforeTransfer = accountService.getById(toAccountId);
        final BigDecimal expectedFromAmount = fromAccountBeforeTransfer.getAmount().subtract(amount);
        final BigDecimal expectedToAmount = toAccountBeforeTransfer.getAmount().add(amount);

        manageFundsService.transfer(fromAccountId, toAccountId, amount, currency);

        final Account fromAccountAfterTransfer = accountService.getById(fromAccountId);
        final Account toAccountAfterTransfer = accountService.getById(toAccountId);

        assertEquals(fromAccountAfterTransfer.getAmount(), expectedFromAmount);
        assertEquals(toAccountAfterTransfer.getAmount(), expectedToAmount);
    }

    @DataProvider
    public static Object[][] invalidTransferData() {
        return new Object[][]{
                {1, 1, BigDecimal.valueOf(100L), RUB},
                {1, 2, BigDecimal.valueOf(0L), EUR},
                {1, 2, BigDecimal.valueOf(-1L), USD},
                {5, 6, BigDecimal.valueOf(250000L), RUB},
                {5, 6, null, EUR},
                {6, 5, BigDecimal.valueOf(12L), null}
        };
    }

    /**
     * Checking that transferring won't success on invalid data
     */
    @Test(dataProvider = "invalidTransferData", expectedExceptions = IllegalArgumentException.class)
    public void invalidTransferTest(Integer fromAccountId, Integer toAccountId, BigDecimal amount, Currency currency) {
        manageFundsService.transfer(fromAccountId, toAccountId, amount, currency);
    }

}
