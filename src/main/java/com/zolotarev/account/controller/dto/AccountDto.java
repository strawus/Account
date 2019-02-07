package com.zolotarev.account.controller.dto;

import com.zolotarev.account.controller.validation.EnumMember;
import com.zolotarev.account.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

import static com.zolotarev.constant.Messages.*;

/**
 * Transport - representation of an account entity
 */
@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Integer id;
    @NotNull(message = AMOUNT_MUST_BE_NOT_NULL)
    @PositiveOrZero(message = AMOUNT_MUST_BE_NOT_NEGATIVE)
    private BigDecimal amount;
    @EnumMember(value = Currency.class, message = UNKNOWN_CURRENCY_CODE)
    private String currencyCode;
}
