package com.zolotarev.account.client;

import com.zolotarev.account.controller.AdviceController;
import com.zolotarev.account.domain.Currency;
import com.zolotarev.exception.RequestException;
import com.zolotarev.mock.controller.CurrencyRateControllerMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.zolotarev.account.domain.Currency.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Test class for {@link CurrencyRateClient}
 */
@ActiveProfiles("dev,mockCurrencyRate")
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@SpringBootTest(webEnvironment = DEFINED_PORT, classes = {CurrencyRateClient.class, CurrencyRateControllerMock.class, AdviceController.class})
public class CurrencyRateClientTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CurrencyRateClient client;

    /*
     * Test data contains all combinations of currency names
     */
    @DataProvider
    public static Object[][] currenciesData() {
        return Arrays.stream(Currency.values())
                .flatMap(first -> Arrays.stream(Currency.values())
                        .map(second -> new Currency[]{first, second}))
                .filter(pair -> pair[0] != pair[1])
                .toArray(Currency[][]::new);
    }

    /**
     * Checking getting currency rates by valid currency data
     */
    @Test(dataProvider = "currenciesData")
    public void sendRequestTest(Currency first, Currency second) {
        final BigDecimal responseRate = client.getRate(first, second).block();
        assertNotNull(responseRate);
        assertTrue(responseRate.compareTo(BigDecimal.ZERO) > 0);
    }

    @DataProvider
    public static Object[][] currenciesInvalidData() {
        return new Object[][]{
                {RUB, RUB},
                {USD, null},
                {null, EUR}
        };
    }

    /**
     * Checking getting currency rates by invalid data
     */
    @Test(dataProvider = "currenciesInvalidData", expectedExceptions = {RequestException.class, IllegalArgumentException.class})
    public void sendInvalidRequestTest(Currency first, Currency second) {
        client.getRate(first, second).block();
    }

}
