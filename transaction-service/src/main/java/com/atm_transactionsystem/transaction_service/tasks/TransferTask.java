package com.atm_transactionsystem.transaction_service.tasks;

import java.util.concurrent.Callable;

import com.atm_transactionsystem.transaction_service.dto.DepositRequest;
import com.atm_transactionsystem.transaction_service.dto.TransferRequest;
import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;
import com.atm_transactionsystem.transaction_service.feign.AccountClient;

public class TransferTask implements Callable<String> {

    private final AccountClient accountClient;
    private final TransferRequest request;

    public TransferTask(AccountClient accountClient,
                        TransferRequest request) {

        this.accountClient = accountClient;
        this.request = request;
    }

    @Override
    public String call() throws Exception {

        WithdrawRequest withdraw = new WithdrawRequest();
        withdraw.setAccountNumber(request.getFromAccount());
        withdraw.setAmount(request.getAmount());

        accountClient.withdraw(withdraw);

        DepositRequest deposit = new DepositRequest();
        deposit.setAccountNumber(request.getToAccount());
        deposit.setAmount(request.getAmount());

        accountClient.deposit(deposit);

        return "Transfer Successful";

    }
}