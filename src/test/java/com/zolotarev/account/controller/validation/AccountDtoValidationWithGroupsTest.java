package com.zolotarev.account.controller.validation;

import com.zolotarev.account.controller.dto.AccountDto;
import org.testng.annotations.DataProvider;

import java.math.BigDecimal;

import static com.zolotarev.account.domain.Currency.RUB;

/**
 * Test class for {@link AccountDto} validation with groups
 */
public class AccountDtoValidationWithGroupsTest extends AbstractValidationWithGroupsTest {

    /**
     * @return Account dto for validation in format:
     * Dto for testing, expected validation result, validation groups
     */
    @DataProvider
    public static Object[][] dtosWithGroups() {
        return new Object[][]{
                {validDto(), true, AccountDto.UpdateAccount.class},
                {validDto().withId(null).withVersion(null), true, AccountDto.CreateAccount.class},
                {validDto().withId(null), false, AccountDto.UpdateAccount.class},
                {validDto().withId(null), false, AccountDto.CreateAccount.class},
                {validDto().withVersion(null), false, AccountDto.UpdateAccount.class},
                {validDto().withVersion(null), false, AccountDto.CreateAccount.class}
        };
    }

    private static AccountDto validDto() {
        return new AccountDto(1, BigDecimal.TEN, RUB.name(), 0);
    }
}
