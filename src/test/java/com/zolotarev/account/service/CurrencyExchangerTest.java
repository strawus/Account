package com.zolotarev.account.service;

import com.zolotarev.account.client.CurrencyRateClient;
import com.zolotarev.account.domain.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.EUR;
import static com.zolotarev.account.domain.Currency.RUB;
import static com.zolotarev.account.domain.Currency.USD;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Test class for {@link CurrencyExchanger}
 */
@SpringBootTest(classes = CurrencyExchanger.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class CurrencyExchangerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CurrencyExchanger exchanger;
    @MockBean
    private CurrencyRateClient client;

    /**
     * @return Data for testing currency exchanging in format:
     * Amount to exchange, amount currency, currency to exchange, currency rate, expected amount after exchange
     */
    @DataProvider
    public static Object[][] currencyExchangerData() {
        return new Object[][]{
                {ONE, RUB, RUB, ZERO, ONE},
                {BigDecimal.valueOf(100L), EUR, USD, BigDecimal.valueOf(2L), BigDecimal.valueOf(20000L, 2)},
                {TEN, RUB, USD, BigDecimal.valueOf(1L, 1), BigDecimal.valueOf(100L, 2)}
        };
    }

    /**
     * Checking right calculating amount according to the results of currencies exchange
     */
    @Test(dataProvider = "currencyExchangerData")
    public void exchangeTest(BigDecimal amount, Currency fromCurrency, Currency toCurrency, BigDecimal currencyRate, BigDecimal expectedAmount) {
        when(client.getRate(fromCurrency, toCurrency)).thenReturn(Mono.just(currencyRate));

        final BigDecimal exchangedAmount = exchanger.exchange(amount, fromCurrency, toCurrency);

        assertEquals(exchangedAmount, expectedAmount);
    }

    @DataProvider
    public static Object[][] currencyExchangerInvalidData() {
        return new Object[][]{
                {null, RUB, USD},
                {ONE, null, EUR},
                {TEN, USD, null},
                {ZERO, USD, EUR},
                {BigDecimal.valueOf(-1L), EUR, RUB}
        };
    }

    /**
     * Checking throwing an exception on invalid data
     */
    @Test(dataProvider = "currencyExchangerInvalidData", expectedExceptions = IllegalArgumentException.class)
    public void invalidExchangeTest(BigDecimal amount, Currency fromCurrency, Currency toCurrency) {
        exchanger.exchange(amount, fromCurrency, toCurrency);
    }
}
