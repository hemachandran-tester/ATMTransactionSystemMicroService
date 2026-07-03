package com.atm_transactionsystem.account_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.atm_transactionsystem.account_service.dto.CreateAccountRequest;
import com.atm_transactionsystem.account_service.dto.DepositRequest;
import com.atm_transactionsystem.account_service.dto.DepositResponse;
import com.atm_transactionsystem.account_service.dto.WithdrawRequest;
import com.atm_transactionsystem.account_service.dto.WithdrawResponse;
import com.atm_transactionsystem.account_service.entity.Account;
import com.atm_transactionsystem.account_service.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountController {

    @Autowired
    private AccountService service;

    // Creating and Account
    @PostMapping
    public ResponseEntity<String> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        return new ResponseEntity<>(
                service.createAccount(request),
                HttpStatus.CREATED);
    }

    // Search the Account
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> searchAccount(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                service.getAccount(accountNumber));
    }

    // Deposit money
    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(
            @Valid @RequestBody DepositRequest request) {

        return ResponseEntity.ok(
                service.deposit(request));
    }

    // Withdraw money
    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(
            @Valid @RequestBody WithdrawRequest request) {

        return ResponseEntity.ok(
                service.withdraw(request));
    }

    // to check Balance
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<Double> balance(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                service.checkBalance(accountNumber));
    }
}