package com.zolotarev.account.controller.validation;

import com.zolotarev.account.controller.dto.AccountDto;
import org.testng.annotations.DataProvider;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.*;

/**
 * Test class for {@link AccountDto} validation
 */
public class AccountDtoValidationTest extends AbstractValidationTest {

    /**
     * @return Account dto for validation in format:
     * Dto for testing, expected validation result
     */
    @DataProvider
    public static Object[][] dtos() {
        return new Object[][]{
                {validDto().withAmount(BigDecimal.ZERO), true},
                {validDto().withAmount(null), false},
                {validDto().withAmount(BigDecimal.valueOf(-1L)), false},
                {validDto().withCurrencyCode(null), false},
                {validDto().withCurrencyCode("invalid"), false}
        };
    }

    private static AccountDto validDto() {
        return new AccountDto(1, BigDecimal.TEN, RUB.name(), 0);
    }
}
