package com.zolotarev.account.controller.validation;

import com.zolotarev.account.controller.dto.ManageFundsDto;
import org.testng.annotations.DataProvider;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.RUB;

/**
 * Test class for {@link ManageFundsDto} validation
 */
public class ManageFundsDtoValidationTest extends AbstractValidationTest {

    @DataProvider
    public static Object[][] dtos() {
        return new Object[][]{
                {validDto(), true},
                {validDto().withAccountId(null), false},
                {validDto().withAmount(null), false},
                {validDto().withAmount(BigDecimal.ZERO), false},
                {validDto().withAmount(BigDecimal.valueOf(-1L)), false},
                {validDto().withCurrencyCode(null), false},
                {validDto().withCurrencyCode("invalid"), false}
        };
    }

    private static ManageFundsDto validDto() {
        return new ManageFundsDto(1, BigDecimal.TEN, RUB.name());
    }

}
