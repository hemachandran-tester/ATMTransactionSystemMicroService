package com.atm_transactionsystem.transaction_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm_transactionsystem.transaction_service.dto.DepositRequest;
import com.atm_transactionsystem.transaction_service.dto.TransferRequest;
import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;
import com.atm_transactionsystem.transaction_service.entity.Transaction;
import com.atm_transactionsystem.transaction_service.repository.TransactionRepository;
import com.atm_transactionsystem.transaction_service.tasks.DepositTask;
import com.atm_transactionsystem.transaction_service.tasks.TransferTask;
import com.atm_transactionsystem.transaction_service.tasks.WithdrawTask;
import com.atm_transactionsystem.transaction_service.feign.AccountClient;
import com.atm_transactionsystem.transaction_service.dto.DepositResponse;
import com.atm_transactionsystem.transaction_service.dto.WithdrawResponse;
@Service
public class TransactionService {

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private TransactionRepository repository;

    private final ExecutorService executor =
            Executors.newFixedThreadPool(5);
    public DepositResponse deposit(DepositRequest request) throws Exception {

        DepositTask task = new DepositTask(accountClient, request);

        Future<DepositResponse> future = executor.submit(task);

        DepositResponse response = future.get();

        Transaction transaction = new Transaction();

        transaction.setAccountNumber(response.getAccountNumber());
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(response.getAmount());

        transaction.setBalanceAfterTransaction(
                response.getBalanceAfterTransaction());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        repository.save(transaction);

        return response;
    }

    public WithdrawResponse withdraw(WithdrawRequest request) throws Exception {

        WithdrawTask task = new WithdrawTask(accountClient, request);

        Future<WithdrawResponse> future = executor.submit(task);

        // Wait for Account Service to complete the withdrawal
        WithdrawResponse response = future.get();

        Transaction transaction = new Transaction();

        transaction.setAccountNumber(response.getAccountNumber());
        transaction.setTransactionType("WITHDRAW");
        transaction.setAmount(response.getAmount());

        transaction.setBalanceAfterTransaction(
                response.getBalanceAfterTransaction());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        repository.save(transaction);

        return response;
    }

    public String transfer(TransferRequest request) throws Exception {

        TransferTask task = new TransferTask(accountClient, request);

        Future<String> future = executor.submit(task);

        return future.get();
    }

    public List<Transaction> history(String accountNumber) {

        return repository.findByAccountNumber(accountNumber);

    }

}