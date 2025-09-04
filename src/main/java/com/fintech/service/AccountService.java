package com.fintech.service;

import com.fintech.dto.AccountCreateRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.dto.AccountUpdateRequest;
import com.fintech.entity.Account;
import com.fintech.exception.AccountNotFoundException;
import com.fintech.exception.DuplicatePhoneNumberException;
import com.fintech.mapper.AccountMapper;
import com.fintech.repository.AccountRepository;
import com.fintech.util.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountResponse createAccount(AccountCreateRequest request) {
        log.info("Creating account: name={}", request.getName());

        validatePhoneNumber(request.getPhoneNr());

        Account account = accountMapper.toEntity(request);
        Account savedAccount = accountRepository.save(account);

        log.info("Account created: id={}", savedAccount.getId());
        return accountMapper.toResponse(savedAccount);
    }

    @Transactional
    public AccountResponse updateAccount(Long id, AccountUpdateRequest request) {
        log.info("Updating account: id={}", id);

        Account account = findAccountById(id);

        account.updateName(request.getName());
        validateAndUpdatePhoneNumber(account, request.getPhoneNr());

        log.info("Account updated: id={}", id);
        return accountMapper.toResponse(account);
    }

    @Transactional
    public void deleteAccount(Long id) {
        log.info("Deleting account: id={}", id);

        Account account = findAccountById(id);
        account.softDelete();

        log.info("Account deleted: id={}", id);
    }

    public AccountResponse findById(Long id) {
        Account account = findAccountById(id);
        return accountMapper.toResponse(account);
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    private void validatePhoneNumber(String phoneNr) {
        if (StringUtils.hasText(phoneNr)) {
            validatePhoneNumberFormat(phoneNr);
            checkPhoneNumberNotExists(phoneNr);
        }
    }

    private void validatePhoneNumberFormat(String phoneNr) {
        if (!PhoneNumberValidator.isValidE164(phoneNr)) {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNr);
        }
    }

    private void checkPhoneNumberNotExists(String phoneNr) {
        if (accountRepository.existsByPhoneNr(phoneNr)) {
            throw new DuplicatePhoneNumberException(phoneNr);
        }
    }

    private void validateAndUpdatePhoneNumber(Account account, String phoneNr) {
        if (account.isPhoneNumberChanged(phoneNr)) {
            validatePhoneNumber(phoneNr);
            account.updatePhoneNumber(phoneNr);
        }
    }
}