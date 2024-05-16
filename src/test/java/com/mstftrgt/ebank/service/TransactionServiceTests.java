package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.TransactionCustomerDto;
import com.mstftrgt.ebank.dto.model.TransactionDto;
import com.mstftrgt.ebank.dto.request.NewMoneyTransferRequest;
import com.mstftrgt.ebank.exception.AccountNotFoundException;
import com.mstftrgt.ebank.exception.InsufficientBalanceException;
import com.mstftrgt.ebank.model.Account;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.model.Transaction;
import com.mstftrgt.ebank.model.Transaction.TransactionType;
import com.mstftrgt.ebank.repository.AccountRepository;
import com.mstftrgt.ebank.repository.CustomerRepository;
import com.mstftrgt.ebank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ModelMapper modelMapper;
    private AccountService accountService;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        accountService = Mockito.spy(new AccountService(modelMapper, accountRepository, transactionRepository));
        transactionService = new TransactionService(accountRepository, transactionRepository, customerRepository, accountService, modelMapper);
    }


    @Test
    void shouldTransferMoney_whenTheSenderAccountIsPresentAndBalanceIsEnoughAndTheReceiverAccountIsPresent() {

        String senderAccountId = "senderAccountId";
        String senderCustomerId = "senderCustomerId";
        TransactionType transactionType = TransactionType.TRANSFER;

        NewMoneyTransferRequest transferRequest = new NewMoneyTransferRequest("receiverAccountNumber", BigDecimal.TEN, "description");

        Account senderAccount = new Account(senderAccountId, senderCustomerId, "senderAccountNumber",
                BigDecimal.valueOf(100), LocalDateTime.now().minusDays(100));
        Account receiverAccount = new Account("receiverAccountId", "receiverCustomerId",
                transferRequest.getReceiverAccountNumber(), BigDecimal.ZERO, LocalDateTime.now().minusDays(100));


        when(accountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findAccountByAccountNumber(transferRequest.getReceiverAccountNumber())).thenReturn(Optional.of(receiverAccount));

        transactionService.transferMoney(transferRequest, senderAccountId, senderCustomerId);

        verify(accountService).findAccountById(senderAccountId, senderCustomerId);
        verify(accountRepository).findAccountByAccountNumber(transferRequest.getReceiverAccountNumber());

        verify(accountRepository, new Times(2)).save(accountCaptor.capture());

        Account capturedSenderAccount = accountCaptor.getAllValues().get(0);
        assertThat(capturedSenderAccount.getId()).isEqualTo(senderAccount.getId());
        assertThat(capturedSenderAccount.getBalance()).isEqualTo(BigDecimal.valueOf(90));

        Account capturedReceiverAccount = accountCaptor.getAllValues().get(1);
        assertThat(capturedReceiverAccount.getId()).isEqualTo(receiverAccount.getId());
        assertThat(capturedReceiverAccount.getBalance()).isEqualTo(BigDecimal.TEN);

        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction transaction = transactionCaptor.getValue();

        assertThat(transaction.getTransactionType()).isEqualTo(transactionType);
        assertThat(transaction.getDescription()).isEqualTo(transferRequest.getDescription());
        assertThat(transaction.getReceiverAccount()).isEqualTo(capturedReceiverAccount);
        assertThat(transaction.getSenderAccount()).isEqualTo(capturedSenderAccount);
        assertThat(transaction.getAmount()).isEqualTo(transferRequest.getAmount());
    }

    @Test
    void shouldNotTransferMoney_whenTheSenderAccountNotFound() {

        String senderAccountId = "senderAccountId";
        String senderCustomerId = "senderCustomerId";
        NewMoneyTransferRequest transferRequest = new NewMoneyTransferRequest("receiverAccountNumber", BigDecimal.TEN, "description");

        when(accountRepository.findById(senderAccountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.transferMoney(transferRequest, senderAccountId, senderCustomerId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for id : " + senderAccountId);

        Mockito.verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldNotTransferMoney_whenTheSenderAccountFoundButCustomerIdDoesNotMatch() {
        String senderAccountId = "senderAccountId";
        String senderCustomerId = "senderCustomerId";
        NewMoneyTransferRequest transferRequest = new NewMoneyTransferRequest("receiverAccountNumber", BigDecimal.TEN, "description");
        Account account = new Account(senderAccountId, "wrongCustomerId", "senderAccountNumber",
                BigDecimal.valueOf(100), LocalDateTime.now().minusDays(100));

        when(accountRepository.findById(senderAccountId)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> transactionService.transferMoney(transferRequest, senderAccountId, senderCustomerId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for id : " + senderAccountId);

        Mockito.verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldNotTransferMoney_whenTheSenderAccountIsPresentButBalanceIsNotEnough() {

        String senderAccountId = "senderAccountId";
        String senderCustomerId = "senderCustomerId";
        NewMoneyTransferRequest transferRequest = new NewMoneyTransferRequest("receiverAccountNumber", BigDecimal.TEN, "description");

        Account senderAccount = new Account(senderAccountId, senderCustomerId, "senderAccountNumber",
                BigDecimal.ZERO, LocalDateTime.now().minusDays(100));

        when(accountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderAccount));

        assertThatThrownBy(() -> transactionService.transferMoney(transferRequest, senderAccountId, senderCustomerId))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance.");


        Mockito.verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldNotTransferMoney_whenTheSenderAccountIsPresentAndBalanceIsEnoughButTheReceiverAccountNotFound() {

        String senderAccountId = "senderAccountId";
        String senderCustomerId = "senderCustomerId";
        NewMoneyTransferRequest transferRequest = new NewMoneyTransferRequest("receiverAccountNumber", BigDecimal.TEN, "description");

        Account senderAccount = new Account(senderAccountId, senderCustomerId, "senderAccountNumber",
                BigDecimal.valueOf(100), LocalDateTime.now().minusDays(100));

        when(accountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findAccountByAccountNumber(transferRequest.getReceiverAccountNumber())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.transferMoney(transferRequest, senderAccountId, senderCustomerId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for the given account number.");

        Mockito.verifyNoInteractions(transactionRepository);
    }


    @Test
    void shouldReturnTransactionHistory_whenTheAccountFoundAndCustomerIdMatches() {
        String accountId = "accountId";
        String customerId = "customerId";
        String otherAccountId = "otherAccountId";
        String otherCustomerId = "otherCustomerId";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));

        Account account = new Account(accountId, customerId, "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(45));
        Account receiverAccount = new Account(otherAccountId, otherCustomerId, "accountNumber", BigDecimal.TEN, LocalDateTime.now().minusDays(30));

        Transaction transaction = new Transaction("transactionId", account, receiverAccount, TransactionType.TRANSFER, BigDecimal.TEN, LocalDateTime.now().minusDays(30),"description");
        List<Transaction> transactionList = List.of(transaction);
        Page<Transaction> transactionPage = new PageImpl<>(transactionList, pageRequest, transactionList.size());

        Customer sender = new Customer(customerId, "sender@gmail.com", "john", "doe", "123456789", LocalDateTime.now().minusYears(20));
        Customer receiver = new Customer(otherCustomerId, "receiver@gmail.com", "mary", "flower", "123456789", LocalDateTime.now().minusYears(20));

        TransactionCustomerDto senderTransactionCustomerDto = new TransactionCustomerDto(customerId, "john", "doe");
        TransactionCustomerDto receiverTransactionCustomerDto = new TransactionCustomerDto(otherCustomerId, "mary", "flower");
        TransactionDto transactionDto = new TransactionDto(transaction.getTransactionType(), transaction.getAmount(), transaction.getDate(), transaction.getDescription(), senderTransactionCustomerDto, receiverTransactionCustomerDto);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findBySenderAccountIdOrReceiverAccountId(accountId, accountId, pageRequest)).thenReturn(transactionPage);
        when(customerRepository.findById("customerId")).thenReturn(Optional.of(sender));
        when(customerRepository.findById("otherCustomerId")).thenReturn(Optional.of(receiver));
        when(modelMapper.map(transaction, TransactionDto.class)).thenReturn(transactionDto);
        when(modelMapper.map(sender, TransactionCustomerDto.class)).thenReturn(senderTransactionCustomerDto);
        when(modelMapper.map(receiver, TransactionCustomerDto.class)).thenReturn(receiverTransactionCustomerDto);

        List<TransactionDto> result = transactionService.getTransactionHistory(accountId, customerId, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository).findBySenderAccountIdOrReceiverAccountId(accountId, accountId, pageRequest);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).findById(otherCustomerId);
        verify(modelMapper, times(1)).map(sender, TransactionCustomerDto.class);
        verify(modelMapper, times(1)).map(receiver, TransactionCustomerDto.class);
    }

    @Test
    void shouldNotReturnTransactionHistory_whenTheAccountNotFound() {
        String accountId = "accountId";
        String customerId = "customerId";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransactionHistory(accountId, customerId, pageRequest))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for id : " + accountId);

        verifyNoInteractions(customerRepository);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(modelMapper);


    }

    @Test
    void shouldNotReturnTransactionHistory_whenTheAccountFoundButTheCustomerIdDoesNotMatch() {
        String accountId = "accountId";
        String customerId = "customerId";
        String wrongCustomerId = "wrongCustomerId";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));

        Account account = new Account(accountId, wrongCustomerId, "accountNumber", BigDecimal.ZERO, LocalDateTime.now().minusDays(45));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> transactionService.getTransactionHistory(accountId, customerId, pageRequest))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for id : " + accountId);

        verifyNoInteractions(customerRepository);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(modelMapper);
    }

}
