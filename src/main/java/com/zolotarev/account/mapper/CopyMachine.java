package com.zolotarev.account.mapper;

import com.zolotarev.account.domain.Account;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.ERROR;

/**
 * Using code generation technology to create copies of objects
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface CopyMachine {

    default Account copy(Account account) {
        return new Account(account.getId(), account.getAmount(), account.getCurrency());
    }
}
