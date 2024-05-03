package com.mstftrgt.ebank.service;


import com.mstftrgt.ebank.dto.model.AccountDto;
import com.mstftrgt.ebank.dto.model.CustomerDto;
import com.mstftrgt.ebank.dto.request.NewAccountRequestDto;
import com.mstftrgt.ebank.exception.AccountNotFoundException;
import com.mstftrgt.ebank.model.Account;
import com.mstftrgt.ebank.model.Customer;
import com.mstftrgt.ebank.model.Transaction;
import com.mstftrgt.ebank.repository.AccountRepository;
import com.mstftrgt.ebank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public AccountDto createNewAccount(NewAccountRequestDto newAccountRequest) {

        Account newAccount = new Account();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        newAccount.setCustomer(customer);
        newAccount.setBalance(newAccountRequest.getInitialBalance());
        newAccount.setAccountNumber(generateAccountNumber());

        Account account = accountRepository.save(newAccount);

        if (newAccountRequest.getInitialBalance().compareTo(BigDecimal.ZERO) > 0) {

            Transaction transaction = new Transaction();
            transaction.setSenderAccount(account);
            transaction.setReceiverAccount(account);
            transaction.setAmount(newAccountRequest.getInitialBalance());
            transaction.setDescription("Initial transaction.");
            transaction.setTransactionType(Transaction.TransactionType.INITIAL);

            transactionRepository.save(transaction);
        }

        return modelMapper.map(account, AccountDto.class);

    }

    protected String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public AccountDto getAccountById(String accountId, String customerId) {
        Account account = findAccountById(accountId, customerId);
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        accountDto.setCustomerDto(modelMapper.map(account.getCustomer(), CustomerDto.class));
        return accountDto;
    }

    protected Account findAccountById(String accountId, String customerId) {

        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty())
            throw new AccountNotFoundException("Account not found for id : " + accountId);

        Account account = accountOptional.get();

        if (!account.getCustomer().getId().equals(customerId))
            throw new AccountNotFoundException("Account not found for id : " + accountId);  // least privilege principle


        return account;
    }

    public List<AccountDto> getAllAccounts(Customer customer) {

        List<Account> accounts = accountRepository.findAllByCustomerId(customer.getId());

        return accounts
                .stream()
                .map(account -> {
                    AccountDto accountDto = modelMapper.map(account, AccountDto.class);
                    accountDto.setCustomerDto(modelMapper.map(customer, CustomerDto.class));
                    return accountDto;
                })
                .collect(Collectors.toList());
    }

    public void deleteAccountById(String id, String customerId) {
        Account account = findAccountById(id, customerId);
        accountRepository.delete(account);
    }


}
