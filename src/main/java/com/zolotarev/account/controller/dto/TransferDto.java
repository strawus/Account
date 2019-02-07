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
 * Transport contains data for funds transfer
 */
@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
    @NotNull(message = SOURCE_ACCOUNT_ID_MUST_BE_NOT_NULL)
    private Integer sourceAccountId;
    @NotNull(message = TARGET_ACCOUNT_ID_MUST_BE_NOT_NULL)
    private Integer targetAccountId;
    @NotNull(message = AMOUNT_MUST_BE_NOT_NULL)
    @Positive(message = AMOUNT_MUST_BE_POSITIVE)
    private BigDecimal amount;
    @EnumMember(value = Currency.class, message = UNKNOWN_CURRENCY_CODE)
    private String currencyCode;
}
