package com.zolotarev.account.controller.dto;

import com.zolotarev.account.controller.validation.EnumMember;
import com.zolotarev.account.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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

    @Null(groups = CreateAccount.class, message = ACCOUNT_ID_MUST_BE_NULL)
    @NotNull(groups = UpdateAccount.class, message = ACCOUNT_ID_MUST_BE_NOT_NULL)
    private Integer id;

    @NotNull(message = AMOUNT_MUST_BE_NOT_NULL)
    @PositiveOrZero(message = AMOUNT_MUST_BE_NOT_NEGATIVE)
    private BigDecimal amount;

    @EnumMember(value = Currency.class, message = UNKNOWN_CURRENCY_CODE)
    private String currencyCode;

    @Null(groups = CreateAccount.class, message = VERSION_MUST_BE_NULL)
    @NotNull(groups = UpdateAccount.class, message = VERSION_MUST_BE_NOT_NULL)
    private Integer version;


    public interface CreateAccount { }
    public interface UpdateAccount { }
}
