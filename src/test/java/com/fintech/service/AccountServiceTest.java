package com.fintech.service;

import com.fintech.dto.AccountCreateRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.dto.AccountUpdateRequest;
import com.fintech.entity.Account;
import com.fintech.exception.AccountNotFoundException;
import com.fintech.exception.DuplicatePhoneNumberException;
import com.fintech.mapper.AccountMapper;
import com.fintech.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService Tests")
class AccountServiceTest {

    private static final Long EXISTENT_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String VALID_PHONE = "+1234567890";
    private static final String ANOTHER_VALID_PHONE = "+9876543210";
    private static final String JOHN_DOE_NAME = "John Doe";
    private static final String JANE_SMITH_NAME = "Jane Smith";

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;
    private AccountCreateRequest createRequest;
    private AccountUpdateRequest updateRequest;
    private AccountResponse testAccountResponse;

    @BeforeEach
    void setUp() {
        testAccount = createTestAccount();
        testAccountResponse = createTestAccountResponse();

        createRequest = new AccountCreateRequest(JOHN_DOE_NAME, VALID_PHONE);
        updateRequest = new AccountUpdateRequest(JANE_SMITH_NAME, ANOTHER_VALID_PHONE);
    }

    @Nested
    @DisplayName("Create Account Tests")
    class CreateAccountTests {

        @Test
        @DisplayName("should_createAccountSuccessfully_when_validRequestProvided")
        void should_createAccountSuccessfully_when_validRequestProvided() {
            when(accountRepository.existsByPhoneNr(createRequest.getPhoneNr())).thenReturn(false);
            when(accountMapper.toEntity(createRequest)).thenReturn(testAccount);
            when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
            when(accountMapper.toResponse(testAccount)).thenReturn(testAccountResponse);

            AccountResponse result = accountService.createAccount(createRequest);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(EXISTENT_ID);
            assertThat(result.getName()).isEqualTo(JOHN_DOE_NAME);
            assertThat(result.getPhoneNr()).isEqualTo(VALID_PHONE);

            verify(accountRepository).existsByPhoneNr(VALID_PHONE);
            verify(accountMapper).toEntity(createRequest);
            verify(accountRepository).save(any(Account.class));
            verify(accountMapper).toResponse(testAccount);
        }

        @Test
        @DisplayName("should_throwIllegalArgumentException_when_phoneNumberAlreadyExists")
        void should_throwIllegalArgumentException_when_phoneNumberAlreadyExists() {
            when(accountRepository.existsByPhoneNr(createRequest.getPhoneNr())).thenReturn(true);

            assertThatThrownBy(() -> accountService.createAccount(createRequest))
                    .isInstanceOf(DuplicatePhoneNumberException.class)
                    .hasMessage("Phone number already exists: " + VALID_PHONE);

            verify(accountRepository, never()).save(any(Account.class));
        }
    }

    @Nested
    @DisplayName("Find Account Tests")
    class FindAccountTests {

        @Test
        @DisplayName("should_returnAccount_when_accountExists")
        void should_returnAccount_when_accountExists() {
            when(accountRepository.findById(EXISTENT_ID)).thenReturn(Optional.of(testAccount));
            when(accountMapper.toResponse(testAccount)).thenReturn(testAccountResponse);

            AccountResponse result = accountService.findById(EXISTENT_ID);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(EXISTENT_ID);
            assertThat(result.getName()).isEqualTo(JOHN_DOE_NAME);
            assertThat(result.getPhoneNr()).isEqualTo(VALID_PHONE);

            verify(accountRepository).findById(EXISTENT_ID);
            verify(accountMapper).toResponse(testAccount);
        }

        @Test
        @DisplayName("should_throwAccountNotFoundException_when_accountDoesNotExist")
        void should_throwAccountNotFoundException_when_accountDoesNotExist() {
            when(accountRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.findById(NON_EXISTENT_ID))
                    .isInstanceOf(AccountNotFoundException.class)
                    .hasMessage("Account not found with ID: " + NON_EXISTENT_ID);

            verify(accountRepository).findById(NON_EXISTENT_ID);
        }
    }

    @Nested
    @DisplayName("Update Account Tests")
    class UpdateAccountTests {

        @Test
        @DisplayName("should_updateAccountSuccessfully_when_validRequestProvided")
        void should_updateAccountSuccessfully_when_validRequestProvided() {
            AccountResponse updatedResponse = AccountResponse.builder()
                    .id(EXISTENT_ID)
                    .name(JANE_SMITH_NAME)
                    .phoneNr(ANOTHER_VALID_PHONE)
                    .createdTime(testAccount.getCreatedTime())
                    .modifiedTime(LocalDateTime.now())
                    .build();

            when(accountRepository.findById(EXISTENT_ID)).thenReturn(Optional.of(testAccount));
            when(accountRepository.existsByPhoneNr(updateRequest.getPhoneNr())).thenReturn(false);
            when(accountMapper.toResponse(any(Account.class))).thenReturn(updatedResponse);

            AccountResponse result = accountService.updateAccount(EXISTENT_ID, updateRequest);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(EXISTENT_ID);
            assertThat(result.getName()).isEqualTo(JANE_SMITH_NAME);
            assertThat(result.getPhoneNr()).isEqualTo(ANOTHER_VALID_PHONE);

            verify(accountRepository).findById(EXISTENT_ID);
            verify(accountRepository).existsByPhoneNr(ANOTHER_VALID_PHONE);
        }

        @Test
        @DisplayName("should_throwAccountNotFoundException_when_accountDoesNotExist")
        void should_throwAccountNotFoundException_when_accountDoesNotExist() {
            when(accountRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.updateAccount(NON_EXISTENT_ID, updateRequest))
                    .isInstanceOf(AccountNotFoundException.class)
                    .hasMessage("Account not found with ID: " + NON_EXISTENT_ID);

            verify(accountRepository, never()).save(any(Account.class));
        }

        @Test
        @DisplayName("should_throwIllegalArgumentException_when_phoneNumberAlreadyExistsForDifferentAccount")
        void should_throwIllegalArgumentException_when_phoneNumberAlreadyExistsForDifferentAccount() {

            when(accountRepository.findById(EXISTENT_ID)).thenReturn(Optional.of(testAccount));
            when(accountRepository.existsByPhoneNr(updateRequest.getPhoneNr())).thenReturn(true);

            assertThatThrownBy(() -> accountService.updateAccount(EXISTENT_ID, updateRequest))
                    .isInstanceOf(DuplicatePhoneNumberException.class)
                    .hasMessage("Phone number already exists: " + ANOTHER_VALID_PHONE);
        }
    }

    @Nested
    @DisplayName("Delete Account Tests")
    class DeleteAccountTests {

        @Test
        @DisplayName("should_softDeleteAccountSuccessfully_when_accountExists")
        void should_softDeleteAccountSuccessfully_when_accountExists() {
            when(accountRepository.findById(EXISTENT_ID)).thenReturn(Optional.of(testAccount));

            accountService.deleteAccount(EXISTENT_ID);

            assertThat(testAccount.getIsActive()).isNull();
            assertThat(testAccount.getDeletedTime()).isNotNull();
        }

        @Test
        @DisplayName("should_throwAccountNotFoundException_when_accountDoesNotExist")
        void should_throwAccountNotFoundException_when_accountDoesNotExist() {
            when(accountRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.deleteAccount(NON_EXISTENT_ID))
                    .isInstanceOf(AccountNotFoundException.class)
                    .hasMessage("Account not found with ID: " + NON_EXISTENT_ID);
        }
    }

    private Account createTestAccount() {
        return Account.builder()
                .id(EXISTENT_ID)
                .name(JOHN_DOE_NAME)
                .phoneNr(VALID_PHONE)
                .createdTime(LocalDateTime.now())
                .modifiedTime(LocalDateTime.now())
                .deletedTime(null)
                .isActive(true)
                .build();
    }

    private AccountResponse createTestAccountResponse() {
        return AccountResponse.builder()
                .id(EXISTENT_ID)
                .name(JOHN_DOE_NAME)
                .phoneNr(VALID_PHONE)
                .createdTime(testAccount != null ? testAccount.getCreatedTime() : LocalDateTime.now())
                .modifiedTime(testAccount != null ? testAccount.getModifiedTime() : LocalDateTime.now())
                .build();
    }
}