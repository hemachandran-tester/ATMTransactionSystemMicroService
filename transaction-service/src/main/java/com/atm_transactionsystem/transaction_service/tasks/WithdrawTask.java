package com.atm_transactionsystem.transaction_service.tasks;

import java.util.concurrent.Callable;

import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;
import com.atm_transactionsystem.transaction_service.feign.AccountClient;
import com.atm_transactionsystem.transaction_service.dto.WithdrawResponse;
public class WithdrawTask implements Callable<WithdrawResponse> {

    private final AccountClient accountClient;
    private final WithdrawRequest request;

    public WithdrawTask(AccountClient accountClient,
                        WithdrawRequest request) {

        this.accountClient = accountClient;
        this.request = request;
    }

    @Override
    public WithdrawResponse call() {
        return accountClient.withdraw(request);
    }
}