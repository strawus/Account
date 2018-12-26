package com.zolotarev.account.controller.validation;

import com.zolotarev.account.controller.dto.TransferDto;
import org.testng.annotations.DataProvider;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.RUB;

/**
 * Test suite for {@link TransferDto} validation
 */
public class TransferDtoValidationTest extends AbstractValidationTest {

    @DataProvider
    public static Object[][] dtos() {
        return new Object[][]{
                {validDto(), true},
                {validDto().withSourceAccountId(null), false},
                {validDto().withTargetAccountId(null), false},
                {validDto().withAmount(null), false},
                {validDto().withAmount(BigDecimal.ZERO), false},
                {validDto().withAmount(BigDecimal.valueOf(-1L)), false},
                {validDto().withCurrencyCode(null), false},
                {validDto().withCurrencyCode("invalid"), false}
        };
    }

    private static TransferDto validDto() {
        return new TransferDto(1, 2, BigDecimal.TEN, RUB.name());
    }
}
