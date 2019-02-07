package com.zolotarev.account.mapper;

import com.zolotarev.account.controller.dto.AccountDto;
import com.zolotarev.account.domain.Account;
import com.zolotarev.account.domain.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.ReportingPolicy.ERROR;

/**
 * Converts account from/to entity {@link Account} to/from dto {@link AccountDto}
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface AccountMapper {

    default Account toEntity(AccountDto accountDto) {
        final Currency currency = Currency.parse(accountDto.getCurrencyCode());
        return new Account(accountDto.getId(), accountDto.getAmount(), currency);
    }

    @Mapping(target = "withId", ignore = true)
    @Mapping(target = "withAmount", ignore = true)
    @Mapping(target = "withCurrencyCode", ignore = true)
    @Mapping(target = "currencyCode", source = "currency")
    AccountDto toDto(Account account);

    List<AccountDto> toDtoList(List<Account> accounts);
}
