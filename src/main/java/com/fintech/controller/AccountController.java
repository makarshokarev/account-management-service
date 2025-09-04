package com.fintech.controller;

import com.fintech.dto.AccountCreateRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.dto.AccountUpdateRequest;
import com.fintech.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.fintech.security.Authority.USER_READ;
import static com.fintech.security.Authority.USER_WRITE;


@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController implements AccountApiInterface {

    private final AccountService accountService;

    @PreAuthorize(USER_WRITE)
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize(USER_READ)
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountResponse response = accountService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(USER_WRITE)
    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountUpdateRequest request) {
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(USER_WRITE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}