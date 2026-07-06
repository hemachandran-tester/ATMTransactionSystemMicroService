package com.atm_transactionsystem.transaction_service.tasks;

import java.util.concurrent.Callable;

import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;
import com.atm_transactionsystem.transaction_service.feign.AccountClient;
import com.atm_transactionsystem.transaction_service.dto.WithdrawResponse;
import com.atm_transactionsystem.transaction_service.dto.AccountWithdrawResponse;
public class WithdrawTask implements Callable<AccountWithdrawResponse> {

    private final AccountClient accountClient;
    private final WithdrawRequest request;

    public WithdrawTask(AccountClient accountClient,
                        WithdrawRequest request) {

        this.accountClient = accountClient;
        this.request = request;
    }

    @Override
    public AccountWithdrawResponse call() {
        return accountClient.withdraw(request);
    }
}