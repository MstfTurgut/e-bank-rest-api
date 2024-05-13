package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.AccountDto;
import com.mstftrgt.ebank.dto.request.NewAccountRequestDto;
import com.mstftrgt.ebank.model.Account;
import com.mstftrgt.ebank.model.Transaction;
import com.mstftrgt.ebank.repository.AccountRepository;
import com.mstftrgt.ebank.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.mstftrgt.ebank.exception.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AccountService accountService;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;


    @DisplayName("Should create new account and save initial transaction when the request is valid and initial balance is bigger than zero.")
    @Test
    public void shouldCreateNewAccountAndSaveInitialTransaction_whenTheRequestIsValidAndInitialBalanceIsBiggerThanZero() {
        String mockCustomerId = "customerId";
        BigDecimal initialBalance = new BigDecimal("100.00");
        NewAccountRequestDto mockRequest = new NewAccountRequestDto(initialBalance);

        Account mockAccount = new Account("accountId" , mockCustomerId, "generatedAccountNumber", initialBalance, LocalDateTime.now());

        AccountDto expectedDto = new AccountDto("accountId", mockCustomerId,
                "generatedAccountNumber", initialBalance, LocalDateTime.now()); // Replace with appropriate data

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);
        Mockito.when(modelMapper.map(mockAccount, AccountDto.class)).thenReturn(expectedDto);

        AccountDto actualDto = accountService.createNewAccount(mockRequest, mockCustomerId);

        assertEquals(expectedDto, actualDto);

        Mockito.verify(accountRepository).save(accountCaptor.capture());
        Account accountValue = accountCaptor.getValue();
        assertThat(accountValue.getCustomerId()).isEqualTo(mockCustomerId);
        assertThat(accountValue.getBalance()).isEqualTo(initialBalance);

        Mockito.verify(transactionRepository).save(transactionCaptor.capture());
        Transaction transactionValue = transactionCaptor.getValue();
        assertThat(transactionValue.getReceiverAccount()).isEqualTo(mockAccount);
        assertThat(transactionValue.getSenderAccount()).isEqualTo(mockAccount);
        assertThat(transactionValue.getAmount()).isEqualTo(initialBalance);
        assertThat(transactionValue.getTransactionType()).isEqualTo(Transaction.TransactionType.INITIAL);
        assertThat(transactionValue.getDescription()).isEqualTo("Initial transaction.");
    }


    @DisplayName("Should create new account and does not save initial transaction when the request is valid and initial balance is zero.")
    @Test
    void shouldCreateNewAccountAndDoesNotSaveInitialTransaction_whenTheRequestIsValidAndInitialBalanceIsZero() {

        String mockCustomerId = "customerId";
        BigDecimal initialBalance = BigDecimal.ZERO;
        NewAccountRequestDto mockRequest = new NewAccountRequestDto(initialBalance);

        Account mockAccount = new Account("accountId" , mockCustomerId, "generatedAccountNumber", initialBalance, LocalDateTime.now());

        AccountDto expectedDto = new AccountDto("accountId", mockCustomerId,
                "generatedAccountNumber", initialBalance, LocalDateTime.now()); // Replace with appropriate data

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);
        Mockito.when(modelMapper.map(mockAccount, AccountDto.class)).thenReturn(expectedDto);

        AccountDto actualDto = accountService.createNewAccount(mockRequest, mockCustomerId);

        assertEquals(expectedDto, actualDto);

        Mockito.verify(accountRepository).save(accountCaptor.capture());
        Account accountValue = accountCaptor.getValue();
        assertThat(accountValue.getCustomerId()).isEqualTo(mockCustomerId);
        assertThat(accountValue.getBalance()).isEqualTo(initialBalance);

        Mockito.verifyNoInteractions(transactionRepository);
    }


    @DisplayName("Should return the requested account when the account found and customer id matches.")
    @Test
    void shouldReturnTheRequestedAccount_whenTheAccountFoundAndCustomerIdMatches() {

        String accountId = "accountId";
        String customerId = "customerId";
        Account mockAccount = new Account(accountId, customerId, "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));
        AccountDto expectedResult = new AccountDto(accountId, customerId, "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        Mockito.when(modelMapper.map(mockAccount, AccountDto.class)).thenReturn(expectedResult);

        assertEquals(expectedResult, accountService.getAccountById(accountId, customerId));

        Mockito.verify(accountRepository).findById(accountId);
        Mockito.verify(modelMapper).map(mockAccount, AccountDto.class);
    }

    @DisplayName("Should not return the account when the account not found.")
    @Test
    void shouldNotReturnTheAccount_whenTheAccountNotFound() {

        String accountId = "accountId";
        String customerId = "customerId";

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(accountId, customerId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for id : " + accountId);

        Mockito.verify(accountRepository).findById(accountId);
        Mockito.verifyNoInteractions(modelMapper);
    }

    @DisplayName("Should not return the account when the account found but customerId does not match.")
    @Test
    void shouldNotReturnTheAccount_whenTheAccountFoundButCustomerIdDoesNotMatch() {

        String accountId = "accountId";
        String customerId = "customerId";
        Account mockAccount = new Account(accountId, "wrongCustomerId", "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        assertThatThrownBy(() -> accountService.getAccountById(accountId, customerId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for id : " + accountId);

        Mockito.verify(accountRepository).findById(accountId);
        Mockito.verifyNoInteractions(modelMapper);
    }

    @DisplayName("Should return all accounts when requested.")
    @Test
    void shouldReturnAllAccounts_whenRequested() {

        String customerId = "customerId";
        Account mockAccount1 = new Account("accountId1", "wrongCustomerId", "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));
        Account mockAccount2 = new Account("accountId2", "wrongCustomerId", "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));
        Account mockAccount3 = new Account("accountId3", "wrongCustomerId", "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));
        List<Account> accounts = Arrays.asList(mockAccount1, mockAccount2, mockAccount3);

        AccountDto accountDto1 = new AccountDto("accountId1", "wrongCustomerId", "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));
        AccountDto accountDto2 = new AccountDto("accountId2", "wrongCustomerId", "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));
        AccountDto accountDto3 = new AccountDto("accountId3", "wrongCustomerId", "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));
        List<AccountDto> expectedResult = Arrays.asList(accountDto1, accountDto2, accountDto3);

        Mockito.when(accountRepository.findAllByCustomerId(customerId)).thenReturn(accounts);
        Mockito.when(modelMapper.map(mockAccount1, AccountDto.class)).thenReturn(accountDto1);
        Mockito.when(modelMapper.map(mockAccount2, AccountDto.class)).thenReturn(accountDto2);
        Mockito.when(modelMapper.map(mockAccount3, AccountDto.class)).thenReturn(accountDto3);

        List<AccountDto> result = accountService.getAllAccounts(customerId);

        assertEquals(expectedResult, result);

        Mockito.verify(accountRepository).findAllByCustomerId(customerId);
        Mockito.verify(modelMapper, new Times(3)).map(Mockito.any(Account.class), Mockito.eq(AccountDto.class));

    }

    @Test
    @DisplayName("Should delete the requested account and initial transaction when the account found and customer id matches and there was an initial transaction.")
    void shouldDeleteTheRequestedAccountAndInitialTransaction_whenTheAccountFoundAndCustomerIdMatchesAndThereWasAnInitialTransaction() {

        String accountId = "accountId";
        String customerId = "customerId";
        Account mockAccount = new Account(accountId, customerId, "accountNumber", BigDecimal.TEN, LocalDateTime.now().minusDays(30));
        Transaction mockInitialTransaction = new Transaction("transactionId", mockAccount, mockAccount, Transaction.TransactionType.INITIAL, BigDecimal.TEN, LocalDateTime.now().minusDays(30), "Initial transaction.");

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        Mockito.when(transactionRepository.findInitialTransactionByAccountId(accountId)).thenReturn(Optional.of(mockInitialTransaction));

        accountService.deleteAccountById(accountId, customerId);

        Mockito.verify(accountRepository).findById(accountId);
        Mockito.verify(transactionRepository).findInitialTransactionByAccountId(accountId);
        Mockito.verify(transactionRepository).delete(mockInitialTransaction);
        Mockito.verify(accountRepository).delete(mockAccount);
    }

    @Test
    @DisplayName("Should delete the requested account but should not delete any transaction when the account found and customer id matches but there was no initial transaction.")
    void shouldDeleteTheRequestedAccountButShouldNotDeleteAnyTransaction_whenTheAccountFoundAndCustomerIdMatchesButThereWasNoInitialTransaction() {

        String accountId = "accountId";
        String customerId = "customerId";
        Account mockAccount = new Account(accountId, customerId, "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        Mockito.when(transactionRepository.findInitialTransactionByAccountId(accountId)).thenReturn(Optional.empty());

        accountService.deleteAccountById(accountId, customerId);

        Mockito.verify(accountRepository).findById(accountId);
        Mockito.verify(transactionRepository).findInitialTransactionByAccountId(accountId);
        Mockito.verify(accountRepository).delete(mockAccount);
    }

    @Test
    @DisplayName("Should not delete any account when the account not found.")
    void shouldNotDeleteAnyAccount_whenTheAccountNotFound() {

        String accountId = "accountId";
        String customerId = "customerId";

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> accountService.deleteAccountById(accountId, customerId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for id : " + accountId);

        Mockito.verify(accountRepository).findById(accountId);
        Mockito.verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("Should not delete the account when the account found but customer id does not match.")
    void shouldNotDeleteTheAccount_whenTheAccountFoundButCustomerIdDoesNotMatch() {

        String accountId = "accountId";
        String customerId = "customerId";
        Account mockAccount = new Account(accountId, "wrongCustomerId", "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(30));

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        assertThatThrownBy(() -> accountService.getAccountById(accountId, customerId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for id : " + accountId);

        Mockito.verify(accountRepository).findById(accountId);
        Mockito.verifyNoInteractions(transactionRepository);
    }


}

