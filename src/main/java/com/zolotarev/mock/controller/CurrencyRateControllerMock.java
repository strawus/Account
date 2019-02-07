package com.zolotarev.mock.controller;

import com.zolotarev.util.Contract;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.zolotarev.constant.Messages.UNKNOWN_CURRENCY_PAIR;

/**
 * Uses instead mock server for simulating currency rates api
 */
@Profile("dev")
@RestController
@RequestMapping("/api/currency-rates")
public class CurrencyRateControllerMock {

    private final Map<String, BigDecimal> currentRate = new ConcurrentHashMap<>();
    {
        currentRate.put("USD-RUB", BigDecimal.valueOf(654L, 1));
        currentRate.put("RUB-USD", BigDecimal.valueOf(15L, 3));
        currentRate.put("EUR-RUB", BigDecimal.valueOf(759L, 1));
        currentRate.put("RUB-EUR", BigDecimal.valueOf(13L, 3));
        currentRate.put("USD-EUR", BigDecimal.valueOf(8797L, 4));
        currentRate.put("EUR-USD", BigDecimal.valueOf(11368L, 4));
    }

    /**
     * Calculates randomized currency rate for incoming pair
     * @param pair Currency pair formatted like example: USD-RUB
     * @return New value of currency rate
     */
    @GetMapping("/{pair}")
    public BigDecimal rate(@PathVariable String pair) {
        return rateByPair(pair.trim().toUpperCase());
    }

    private BigDecimal rateByPair(String pair) {
        final BigDecimal newRate = currentRate.computeIfPresent(pair, (p, oldRate) -> randomizeRate(oldRate));
        Contract.requiresNotNull(newRate, UNKNOWN_CURRENCY_PAIR);
        return newRate;
    }

    private BigDecimal randomizeRate(BigDecimal rate) {
        return rate.add(BigDecimal.valueOf(-9999 + new Random().nextInt(19998), 4)).max(BigDecimal.valueOf(1L, 4));
    }
}
