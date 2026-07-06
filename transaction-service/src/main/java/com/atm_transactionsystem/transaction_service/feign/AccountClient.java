package com.atm_transactionsystem.transaction_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.atm_transactionsystem.transaction_service.dto.AccountDepositResponse;
import com.atm_transactionsystem.transaction_service.dto.AccountWithdrawResponse;
import com.atm_transactionsystem.transaction_service.dto.DepositRequest;
import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;

@FeignClient(name = "account-service")
public interface AccountClient {

    @PostMapping("/accounts/deposit")
    AccountDepositResponse deposit(@RequestBody DepositRequest request);

    @PostMapping("/accounts/withdraw")
    AccountWithdrawResponse withdraw(@RequestBody WithdrawRequest request);

    @GetMapping("/accounts/balance/{accountNumber}")
    Double getBalance(@PathVariable String accountNumber);
}