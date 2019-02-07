package com.zolotarev.account.controller.dto;

import com.zolotarev.account.controller.validation.EnumMember;
import com.zolotarev.account.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static com.zolotarev.constant.Messages.*;

/**
 * Transport contains data for funds management
 */
@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class ManageFundsDto {
    @NotNull(message = ACCOUNT_ID_MUST_BE_NOT_NULL)
    private Integer accountId;
    @NotNull(message = AMOUNT_MUST_BE_NOT_NULL)
    @Positive(message = AMOUNT_MUST_BE_POSITIVE)
    private BigDecimal amount;
    @EnumMember(value = Currency.class, message = UNKNOWN_CURRENCY_CODE)
    private String currencyCode;
}
