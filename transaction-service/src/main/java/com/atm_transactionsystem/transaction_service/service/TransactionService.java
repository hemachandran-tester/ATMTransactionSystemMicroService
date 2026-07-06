package com.atm_transactionsystem.transaction_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm_transactionsystem.transaction_service.dto.AccountDepositResponse;
import com.atm_transactionsystem.transaction_service.dto.AccountWithdrawResponse;
import com.atm_transactionsystem.transaction_service.dto.DepositRequest;
import com.atm_transactionsystem.transaction_service.dto.DepositResponse;
import com.atm_transactionsystem.transaction_service.dto.TransferRequest;
import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;
import com.atm_transactionsystem.transaction_service.dto.WithdrawResponse;
import com.atm_transactionsystem.transaction_service.entity.Transaction;
import com.atm_transactionsystem.transaction_service.feign.AccountClient;
import com.atm_transactionsystem.transaction_service.repository.TransactionRepository;
import com.atm_transactionsystem.transaction_service.tasks.DepositTask;
import com.atm_transactionsystem.transaction_service.tasks.TransferTask;
import com.atm_transactionsystem.transaction_service.tasks.WithdrawTask;

@Service
public class TransactionService {

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private TransactionRepository repository;

    private final ExecutorService executor =
            Executors.newFixedThreadPool(5);

    // --------------------------------------------------
    // Deposit
    // --------------------------------------------------

    public DepositResponse deposit(DepositRequest request) throws Exception {

        DepositTask task = new DepositTask(accountClient, request);

        Future<AccountDepositResponse> future = executor.submit(task);

        AccountDepositResponse accountResponse = future.get();

        Transaction transaction = new Transaction();

        transaction.setAccountNumber(accountResponse.getAccountNumber());
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(accountResponse.getDepositedAmount());
        transaction.setBalanceAfterTransaction(
                accountResponse.getCurrentBalance());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        transaction = repository.save(transaction);

        return new DepositResponse(
                accountResponse.getMessage(),
                transaction.getTransactionId(),
                transaction.getAccountNumber(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getBalanceAfterTransaction(),
                transaction.getStatus(),
                transaction.getTransactionDate()
        );
    }

    // --------------------------------------------------
    // Withdraw
    // --------------------------------------------------

    public WithdrawResponse withdraw(WithdrawRequest request) throws Exception {

        WithdrawTask task = new WithdrawTask(accountClient, request);

        Future<AccountWithdrawResponse> future = executor.submit(task);

        AccountWithdrawResponse accountResponse = future.get();

        Transaction transaction = new Transaction();

        transaction.setAccountNumber(accountResponse.getAccountNumber());
        transaction.setTransactionType("WITHDRAW");
        transaction.setAmount(accountResponse.getWithdrawnAmount());
        transaction.setBalanceAfterTransaction(
                accountResponse.getCurrentBalance());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        transaction = repository.save(transaction);

        return new WithdrawResponse(
                accountResponse.getMessage(),
                transaction.getTransactionId(),
                transaction.getAccountNumber(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getBalanceAfterTransaction(),
                transaction.getStatus(),
                transaction.getTransactionDate()
        );
    }

    // --------------------------------------------------
    // Transfer
    // --------------------------------------------------

    public String transfer(TransferRequest request) throws Exception {

        TransferTask task = new TransferTask(accountClient, request);

        Future<String> future = executor.submit(task);

        return future.get();
    }

    // Transaction History

    public List<Transaction> history(String accountNumber) {

        return repository.findByAccountNumber(accountNumber);
    }
}