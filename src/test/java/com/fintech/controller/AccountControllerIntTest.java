package com.fintech.controller;

import com.fintech.dto.AccountCreateRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.dto.AccountUpdateRequest;
import com.fintech.dto.ErrorResponse;
import com.fintech.entity.Account;
import com.fintech.repository.AccountRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;

import static com.fintech.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountController Integration Tests")
class AccountControllerIntTest extends BaseIntegrationTest {

    private static final Long NON_EXISTENT_ID = 999L;
    private static final String VALID_PHONE = "+1234567890";
    private static final String ANOTHER_VALID_PHONE = "+9876543210";
    private static final String INVALID_PHONE = "123-456-7890";
    private static final String ACCOUNTS_PATH = "/api/v1/accounts";
    private static final String JOHN_DOE_NAME = "John Doe";
    private static final String JANE_SMITH_NAME = "Jane Smith";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    private String accountsUrl;

    @BeforeEach
    void setUp() {
        accountsUrl = baseUrl + ACCOUNTS_PATH;
    }

    @Nested
    @DisplayName("POST /api/accounts - Create Account")
    class CreateAccountTests {

        @Test
        @DisplayName("should_createAccountSuccessfully_when_validRequestProvided")
        void should_createAccountSuccessfully_when_validRequestProvided() {
            accountRepository.deleteAll();

            AccountCreateRequest request = new AccountCreateRequest(JANE_SMITH_NAME, ANOTHER_VALID_PHONE);
            HttpEntity<AccountCreateRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<AccountResponse> response = restTemplate.exchange(
                    accountsUrl, HttpMethod.POST, entity, AccountResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isNotNull();
            assertThat(response.getBody().getName()).isEqualTo(JANE_SMITH_NAME);
            assertThat(response.getBody().getPhoneNr()).isEqualTo(ANOTHER_VALID_PHONE);
            assertThat(response.getBody().getCreatedTime()).isNotNull();
            assertThat(response.getBody().getModifiedTime()).isNotNull();
            assertThat(response.getBody().getDeletedTime()).isNull();
            assertThat(response.getBody().getIsActive()).isTrue();

            assertThat(accountRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("should_returnBadRequest_when_nameIsNull")
        void should_returnBadRequest_when_nameIsNull() {
            accountRepository.deleteAll();

            AccountCreateRequest request = new AccountCreateRequest(null, VALID_PHONE);
            HttpEntity<AccountCreateRequest> entity = new HttpEntity<>(request, headers);

            LocalDateTime start = LocalDateTime.now();

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    accountsUrl, HttpMethod.POST, entity, ErrorResponse.class);

            LocalDateTime end = LocalDateTime.now();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrorCode()).isEqualTo(VALIDATION_FAILED);
            assertThat(response.getBody().getMessage()).contains("Validation failed: name: Name is required");
            assertThat(response.getBody().getTimestamp()).isBetween(start, end);

            assertThat(accountRepository.count()).isEqualTo(0);
        }

        @Test
        @DisplayName("should_returnBadRequest_when_phoneNumberIsInvalidFormat")
        void should_returnBadRequest_when_phoneNumberIsInvalidFormat() {
            accountRepository.deleteAll();

            AccountCreateRequest request = new AccountCreateRequest(JOHN_DOE_NAME, INVALID_PHONE);
            HttpEntity<AccountCreateRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    accountsUrl, HttpMethod.POST, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getMessage())
                    .contains("Invalid phone number format: " + INVALID_PHONE);
        }

        @Test
        @DisplayName("should_returnBadRequest_when_phoneNumberAlreadyExists")
        void should_returnBadRequest_when_phoneNumberAlreadyExists() {
            accountRepository.deleteAll();

            createAccountViaApi(JOHN_DOE_NAME, VALID_PHONE);

            AccountCreateRequest request = new AccountCreateRequest(JANE_SMITH_NAME, VALID_PHONE);
            HttpEntity<AccountCreateRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    accountsUrl, HttpMethod.POST, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrorCode()).isEqualTo(DUPLICATE_PHONE_NUMBER);
            assertThat(response.getBody().getMessage()).isEqualTo("Phone number already exists: " + VALID_PHONE);

            assertThat(accountRepository.count()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("GET /api/accounts/{id} - Get Account By ID")
    class GetAccountByIdTests {

        @Test
        @DisplayName("should_returnAccount_when_accountExists")
        void should_returnAccount_when_accountExists() {
            accountRepository.deleteAll();

            AccountResponse createdAccount = createAccountViaApi(JOHN_DOE_NAME, VALID_PHONE);

            ResponseEntity<AccountResponse> response = restTemplate.getForEntity(
                    accountsUrl + "/" + createdAccount.getId(), AccountResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(createdAccount.getId());
            assertThat(response.getBody().getName()).isEqualTo(JOHN_DOE_NAME);
            assertThat(response.getBody().getPhoneNr()).isEqualTo(VALID_PHONE);
            assertThat(response.getBody().getCreatedTime()).isNotNull();
            assertThat(response.getBody().getModifiedTime()).isNotNull();
            assertThat(response.getBody().getDeletedTime()).isNull();
            assertThat(response.getBody().getIsActive()).isTrue();
        }

        @Test
        @DisplayName("should_returnNotFound_when_accountDoesNotExist")
        void should_returnNotFound_when_accountDoesNotExist() {
            accountRepository.deleteAll();

            LocalDateTime start = LocalDateTime.now();

            ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                    accountsUrl + "/" + NON_EXISTENT_ID, ErrorResponse.class);

            LocalDateTime end = LocalDateTime.now();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrorCode()).isEqualTo(ACCOUNT_NOT_FOUND);
            assertThat(response.getBody().getMessage()).isEqualTo("Account not found with ID: " + NON_EXISTENT_ID);
            assertThat(response.getBody().getTimestamp()).isBetween(start, end);
        }
    }

    @Nested
    @DisplayName("PATCH /api/accounts/{id} - Update Account")
    class UpdateAccountTests {

        @Test
        @DisplayName("should_updateAccountSuccessfully_when_validRequestProvided")
        void should_updateAccountSuccessfully_when_validRequestProvided() {
            accountRepository.deleteAll();

            AccountResponse createdAccount = createAccountViaApi(JOHN_DOE_NAME, VALID_PHONE);
            AccountUpdateRequest request = new AccountUpdateRequest(JANE_SMITH_NAME, ANOTHER_VALID_PHONE);
            HttpEntity<AccountUpdateRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<AccountResponse> response = restTemplate.exchange(
                    accountsUrl + "/" + createdAccount.getId(), HttpMethod.PATCH, entity, AccountResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(createdAccount.getId());
            assertThat(response.getBody().getName()).isEqualTo(JANE_SMITH_NAME);
            assertThat(response.getBody().getPhoneNr()).isEqualTo(ANOTHER_VALID_PHONE);
            assertThat(response.getBody().getCreatedTime()).isNotNull();
            assertThat(response.getBody().getModifiedTime()).isNotNull();
            assertThat(response.getBody().getDeletedTime()).isNull();
            assertThat(response.getBody().getIsActive()).isTrue();

            Account updatedAccount = accountRepository.findById(createdAccount.getId()).orElseThrow();
            assertThat(updatedAccount.getName()).isEqualTo(JANE_SMITH_NAME);
            assertThat(updatedAccount.getPhoneNr()).isEqualTo(ANOTHER_VALID_PHONE);
        }

        @Test
        @DisplayName("should_returnNotFound_when_accountDoesNotExist")
        void should_returnNotFound_when_accountDoesNotExist() {
            accountRepository.deleteAll();

            AccountUpdateRequest request = new AccountUpdateRequest(JANE_SMITH_NAME, ANOTHER_VALID_PHONE);
            HttpEntity<AccountUpdateRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    accountsUrl + "/" + NON_EXISTENT_ID, HttpMethod.PATCH, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getMessage()).isEqualTo("Account not found with ID: " + NON_EXISTENT_ID);
        }

        @Test
        @DisplayName("should_returnBadRequest_when_phoneNumberIsInvalidFormat")
        void should_returnBadRequest_when_phoneNumberIsInvalidFormat() {
            accountRepository.deleteAll();

            AccountResponse createdAccount = createAccountViaApi(JOHN_DOE_NAME, VALID_PHONE);
            AccountUpdateRequest request = new AccountUpdateRequest(JANE_SMITH_NAME, INVALID_PHONE);
            HttpEntity<AccountUpdateRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    accountsUrl + "/" + createdAccount.getId(), HttpMethod.PATCH, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getMessage())
                    .contains("Invalid phone number format: " + INVALID_PHONE);
        }
    }

    @Nested
    @DisplayName("DELETE /api/accounts/{id} - Delete Account")
    class DeleteAccountTests {

        @Test
        @DisplayName("should_deleteAccountSuccessfully_when_accountExists")
        void should_deleteAccountSuccessfully_when_accountExists() {
            accountRepository.deleteAll();

            AccountResponse createdAccount = createAccountViaApi(JOHN_DOE_NAME, VALID_PHONE);
            Long accountId = createdAccount.getId();

            ResponseEntity<Void> response = restTemplate.exchange(
                    accountsUrl + "/" + accountId, HttpMethod.DELETE, null, Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();

            assertThat(accountRepository.findById(accountId)).isEmpty();
        }

        @Test
        @DisplayName("should_returnNotFound_when_accountDoesNotExist")
        void should_returnNotFound_when_accountDoesNotExist() {
            accountRepository.deleteAll();
            LocalDateTime start = LocalDateTime.now();

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    accountsUrl + "/" + NON_EXISTENT_ID, HttpMethod.DELETE, null, ErrorResponse.class);

            LocalDateTime end = LocalDateTime.now();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrorCode()).isEqualTo(ACCOUNT_NOT_FOUND);
            assertThat(response.getBody().getMessage()).isEqualTo("Account not found with ID: " + NON_EXISTENT_ID);
            assertThat(response.getBody().getTimestamp()).isBetween(start, end);
        }
    }

    private AccountResponse createAccountViaApi(String name, String phone) {
        AccountCreateRequest request = new AccountCreateRequest(name, phone);
        HttpEntity<AccountCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AccountResponse> response = restTemplate.exchange(
                accountsUrl, HttpMethod.POST, entity, AccountResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        return response.getBody();
    }
}