package com.atm_transactionsystem.transaction_service.tasks;

import java.util.concurrent.Callable;

import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;
import com.atm_transactionsystem.transaction_service.feign.AccountClient;

public class WithdrawTask implements Callable<String> {

    private final AccountClient accountClient;
    private final WithdrawRequest request;

    public WithdrawTask(AccountClient accountClient,
                        WithdrawRequest request) {

        this.accountClient = accountClient;
        this.request = request;
    }

    @Override
    public String call() {

        return accountClient.withdraw(request);

    }
}