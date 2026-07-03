package com.atm_transactionsystem.transaction_service.tasks;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;

import com.atm_transactionsystem.transaction_service.dto.DepositRequest;
import com.atm_transactionsystem.transaction_service.feign.AccountClient;
import com.atm_transactionsystem.transaction_service.dto.DepositResponse;
public class DepositTask implements Callable<DepositResponse>{

    private final AccountClient accountClient;
    private final DepositRequest request;

    public DepositTask(AccountClient accountClient,
                       DepositRequest request) {

        this.accountClient = accountClient;
        this.request = request;
    }

    @Override
    public DepositResponse call() {
        return accountClient.deposit(request);
    }
}