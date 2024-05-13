package com.mstftrgt.ebank.service;

import com.mstftrgt.ebank.dto.model.TransactionCustomerDto;
import com.mstftrgt.ebank.dto.model.TransactionDto;
import com.mstftrgt.ebank.dto.request.NewMoneyTransferRequestDto;
import com.mstftrgt.ebank.exception.AccountNotFoundException;
import com.mstftrgt.ebank.exception.InsufficientBalanceException;
import com.mstftrgt.ebank.model.Account;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.model.Transaction;
import com.mstftrgt.ebank.repository.AccountRepository;
import com.mstftrgt.ebank.repository.CustomerRepository;
import com.mstftrgt.ebank.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, CustomerRepository customerRepository, AccountService accountService, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    public void transferMoney(NewMoneyTransferRequestDto transferRequestDto, String accountId, String customerId) {

        Account senderAccount = accountService.findAccountById(accountId, customerId);

        if (senderAccount.getBalance().compareTo(transferRequestDto.getAmount()) < 0)
            throw new InsufficientBalanceException("Insufficient balance.");

        senderAccount.setBalance(senderAccount.getBalance().subtract(transferRequestDto.getAmount()));
        accountRepository.save(senderAccount);

        Account receiverAccount = accountRepository
                .findAccountByAccountNumber(transferRequestDto.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found for the given account number."));

        receiverAccount.setBalance(receiverAccount.getBalance().add(transferRequestDto.getAmount()));
        accountRepository.save(receiverAccount);


        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        newTransaction.setDescription(transferRequestDto.getDescription());
        newTransaction.setReceiverAccount(receiverAccount);
        newTransaction.setSenderAccount(senderAccount);
        newTransaction.setAmount(transferRequestDto.getAmount());

        transactionRepository.save(newTransaction);
    }

    public List<TransactionDto> getTransactionHistory(String accountId, String customerId, Pageable pageable) {

        accountService.findAccountById(accountId, customerId);

        Page<Transaction> transactionPage = transactionRepository.findBySenderAccountIdOrReceiverAccountId(accountId, accountId,
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "date"))
                ));

        List<Transaction> transactions = transactionPage.getContent();

        return transactions.stream().map(transaction -> {

            Customer receiverCustomer =
                    customerRepository.findById(transaction.getReceiverAccount().getCustomerId()).orElseThrow();
            Customer senderCustomer =
                    customerRepository.findById(transaction.getSenderAccount().getCustomerId()).orElseThrow();
            TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);
            transactionDto.setSenderCustomer(modelMapper.map(senderCustomer, TransactionCustomerDto.class));
            transactionDto.setReceiverCustomer(modelMapper.map(receiverCustomer, TransactionCustomerDto.class));
            return transactionDto;

        }).toList();

    }
}
