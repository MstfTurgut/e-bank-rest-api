package com.mstftrgt.ebank.service;


import com.mstftrgt.ebank.dto.model.AccountDto;
import com.mstftrgt.ebank.dto.request.NewAccountRequest;
import com.mstftrgt.ebank.exception.AccountNotFoundException;
import com.mstftrgt.ebank.model.Account;
import com.mstftrgt.ebank.model.Transaction;
import com.mstftrgt.ebank.repository.AccountRepository;
import com.mstftrgt.ebank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(ModelMapper modelMapper, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountDto createNewAccount(NewAccountRequest newAccountRequest, String customerId) {

        Account newAccount = new Account();

        newAccount.setCustomerId(customerId);
        newAccount.setBalance(newAccountRequest.getInitialBalance());
        newAccount.setAccountNumber(generateAccountNumber());

        Account account = accountRepository.save(newAccount);

        if (newAccountRequest.getInitialBalance().compareTo(BigDecimal.ZERO) > 0) {

            Transaction transaction =
                    new Transaction(account, account, Transaction.TransactionType.INITIAL,
                            newAccountRequest.getInitialBalance(), "Initial transaction.");

            transactionRepository.save(transaction);
        }

        return modelMapper.map(account, AccountDto.class);
    }

    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public AccountDto getAccountById(String accountId, String customerId) {
        Account account = findAccountById(accountId, customerId);
        return modelMapper.map(account, AccountDto.class);
    }

    protected Account findAccountById(String accountId, String customerId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for id : " + accountId));

        if (!account.getCustomerId().equals(customerId))
            throw new AccountNotFoundException("Account not found for id : " + accountId);  // least privilege principle

        return account;
    }

    public List<AccountDto> getAllAccounts(String customerId) {

        List<Account> accounts = accountRepository.findAllByCustomerId(customerId);

        return accounts
                .stream()
                .map(account ->
                    modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
    }

    public void deleteAccountById(String id, String customerId) {
        Account account = findAccountById(id, customerId);
        Optional<Transaction> initialTransaction = transactionRepository.findInitialTransactionByAccountId(id);
        initialTransaction.ifPresent(transactionRepository::delete);
        accountRepository.delete(account);
    }

}
