package com.zolotarev.account.controller.validation;

import com.zolotarev.account.controller.dto.AccountDto;
import org.testng.annotations.DataProvider;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.*;

/**
 * Test suite for {@link AccountDto} validation
 */
public class AccountDtoValidationTest extends AbstractValidationTest {

    @DataProvider
    public static Object[][] dtos() {
        return new Object[][]{
                {validDto(), true},
                {validDto().withId(null), true},
                {validDto().withAmount(BigDecimal.ZERO), true},
                {validDto().withAmount(null), false},
                {validDto().withAmount(BigDecimal.valueOf(-1L)), false},
                {validDto().withCurrencyCode(null), false},
                {validDto().withCurrencyCode("invalid"), false}
        };
    }

    private static AccountDto validDto() {
        return new AccountDto(1, BigDecimal.TEN, RUB.name());
    }
}
