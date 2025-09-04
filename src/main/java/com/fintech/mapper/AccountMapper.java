package com.fintech.mapper;

import com.fintech.dto.AccountCreateRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountCreateRequest request);
    AccountResponse toResponse(Account account);
}
