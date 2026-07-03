package com.atm_transactionsystem.account_service.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm_transactionsystem.account_service.dto.CreateAccountRequest;
import com.atm_transactionsystem.account_service.dto.DepositRequest;
import com.atm_transactionsystem.account_service.dto.WithdrawRequest;
import com.atm_transactionsystem.account_service.entity.Account;
import com.atm_transactionsystem.account_service.exception.AccountNotFoundException;
import com.atm_transactionsystem.account_service.exception.InsufficientBalanceException;
import com.atm_transactionsystem.account_service.repository.AccountRepository;
import com.atm_transactionsystem.account_service.feign.CustomerClient;
import com.atm_transactionsystem.account_service.dto.DepositResponse;
import com.atm_transactionsystem.account_service.dto.WithdrawResponse;
@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private CustomerClient customerClient;


    // Create Account
    public String createAccount(CreateAccountRequest request) {
        customerClient.getCustomer(request.getCustomerId());
        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setCustomerId(request.getCustomerId());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialBalance());
        account.setStatus("ACTIVE");

        repository.save(account);

        return "Account Created Successfully\nAccount Number : "
                + account.getAccountNumber();
    }
    // Search Account
    public Account getAccount(String accountNumber) {

        return repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account Not Found"));
    }
    // Deposit
    public DepositResponse deposit(DepositRequest request) {

        Account account = getAccount(request.getAccountNumber());

        account.setBalance(
                account.getBalance() + request.getAmount());

        repository.save(account);

        return new DepositResponse(

                "Deposit Successful",

                account.getAccountNumber(),

                request.getAmount(),

                account.getBalance()

        );
    }
    // Withdraw
    public WithdrawResponse withdraw(WithdrawRequest request){

        Account account = getAccount(request.getAccountNumber());

        if (account.getBalance() < request.getAmount()) {

            throw new InsufficientBalanceException(
                    "Insufficient Balance");
        }

        account.setBalance(
                account.getBalance() - request.getAmount());

        repository.save(account);

        return new WithdrawResponse(
                "Withdrawal Successful",
                account.getAccountNumber(),
                request.getAmount(),
                account.getBalance()
        );
    }


    // Check Balance

    public double checkBalance(String accountNumber) {

        Account account = getAccount(accountNumber);

        return account.getBalance();
    }


    // Generate Account Number

    private String generateAccountNumber() {

        Random random = new Random();

        String accountNumber;

        do {

            accountNumber = "10"
                    + (10000000 + random.nextInt(90000000));

        } while (repository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

}