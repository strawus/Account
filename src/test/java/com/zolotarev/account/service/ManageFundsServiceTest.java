package com.zolotarev.account.service;

import com.zolotarev.account.domain.Account;
import com.zolotarev.account.domain.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.*;
import static java.math.BigDecimal.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

/**
 * Test class for {@link ManageFundsService}
 */
@TestExecutionListeners(MockitoTestExecutionListener.class)
@SpringBootTest(classes = ManageFundsService.class)
public class ManageFundsServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ManageFundsService manageFundsService;
    @MockBean
    private AccountService accountService;
    @MockBean
    private CurrencyExchanger currencyExchanger;

    @BeforeClass
    public void setUp() {
        when(currencyExchanger.exchange(any(), any(), any())).then(invocation -> invocation.getArgument(0));
    }

    /**
     * @return Data for testing deposit in format:
     * Account amount before deposit, amount to deposit, expected amount after deposit
     */
    @DataProvider
    public static Object[][] depositAccountData() {
        return new Object[][]{
                {TEN, ONE, TEN.add(ONE)},
                {ZERO, BigDecimal.valueOf(10000L), BigDecimal.valueOf(10000L)}
        };
    }

    /**
     * Checking that amount increase after deposit
     */
    @Test(dataProvider = "depositAccountData")
    public void depositAccountTest(BigDecimal accountAmount, BigDecimal amountToDeposit, BigDecimal expectedAmount) {
        final Account testAccount = new Account(1, accountAmount, RUB);
        when(accountService.getById(testAccount.getId())).thenReturn(testAccount);
        when(accountService.update(any())).then(invocation -> invocation.getArgument(0));

        final Account accountAfterDeposit = manageFundsService.deposit(testAccount.getId(), amountToDeposit, testAccount.getCurrency());

        assertTrue(accountAfterDeposit.getAmount().compareTo(expectedAmount) == 0);
    }

    /**
     * @return Data for testing withdraw in format:
     * Account amount before withdraw, amount to withdraw, expected amount after withdraw
     */
    @DataProvider
    public static Object[][] withdrawAccountData() {
        return new Object[][]{
                {TEN, ONE, TEN.subtract(ONE)},
                {BigDecimal.valueOf(10000L), BigDecimal.valueOf(10000L), ZERO}
        };
    }

    /**
     * Checking that amount decrease after withdraw
     */
    @Test(dataProvider = "withdrawAccountData")
    public void withdrawAccountTest(BigDecimal accountAmount, BigDecimal amountToWithdraw, BigDecimal expectedAmount) {
        final Account testAccount = new Account(2, accountAmount, USD);
        when(accountService.getById(testAccount.getId())).thenReturn(testAccount);
        when(accountService.update(any())).then(invocation -> invocation.getArgument(0));

        final Account accountAfterWithdraw = manageFundsService.withdraw(testAccount.getId(), amountToWithdraw, testAccount.getCurrency());

        assertTrue(accountAfterWithdraw.getAmount().compareTo(expectedAmount) == 0);
    }

    /**
     * @return Data for testing transfer in format:
     * Source account amount before transfer, target account amount before transfer, amount to transfer, expected source account amount, expected target account amount
     */
    @DataProvider
    public static Object[][] transferData() {
        return new Object[][]{
                {TEN, ONE, ONE, TEN.subtract(ONE), ONE.add(ONE)},
                {BigDecimal.valueOf(200L), ZERO, BigDecimal.valueOf(200L), ZERO, BigDecimal.valueOf(200L)}
        };
    }

    /**
     * Checking that amount changed after transfer
     */
    @Test(dataProvider = "transferData")
    public void transferTest(BigDecimal sourceAccountAmount, BigDecimal targetAccountAmount, BigDecimal amountToTransfer,
                             BigDecimal expectedSourceAccountAmount, BigDecimal expectedTargetAccountAmount) {
        final Currency currency = EUR;
        final Account testSourceAccount = new Account(3, sourceAccountAmount, currency);
        final Account testTargetAccount = new Account(4, targetAccountAmount, currency);
        when(accountService.getById(testSourceAccount.getId())).thenReturn(testSourceAccount);
        when(accountService.getById(testTargetAccount.getId())).thenReturn(testTargetAccount);
        when(accountService.update(any())).then(invocation -> invocation.getArgument(0));

        final Account sourceAccountAfterTransfer = manageFundsService.transfer(testSourceAccount.getId(), testTargetAccount.getId(), amountToTransfer, currency);

        assertTrue(sourceAccountAfterTransfer.getAmount().compareTo(expectedSourceAccountAmount) == 0);
        assertTrue(testTargetAccount.getAmount().compareTo(expectedTargetAccountAmount) == 0);
    }

    @DataProvider
    public static Object[][] invalidTransferData() {
        return new Object[][]{
                {1, 1, BigDecimal.valueOf(100L), RUB},
                {1, 2, ZERO, EUR},
                {1, 2, BigDecimal.valueOf(-1L), USD},
                {null, 5, ONE, EUR},
                {6, null, TEN, USD},
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
