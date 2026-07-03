package com.atm_transactionsystem.transaction_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.atm_transactionsystem.transaction_service.dto.DepositRequest;
import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;
import com.atm_transactionsystem.transaction_service.dto.DepositResponse;
import com.atm_transactionsystem.transaction_service.dto.WithdrawResponse;
@FeignClient(name = "account-service")
public interface AccountClient {

    @PostMapping("/accounts/deposit")
    DepositResponse deposit(@RequestBody DepositRequest request);

    @PostMapping("/accounts/withdraw")
    WithdrawResponse withdraw(@RequestBody WithdrawRequest request);

    @GetMapping("/accounts/balance/{accountNumber}")
    Double getBalance(@PathVariable String accountNumber);
}