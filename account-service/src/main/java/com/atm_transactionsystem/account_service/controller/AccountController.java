package com.atm_transactionsystem.account_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.atm_transactionsystem.account_service.dto.CreateAccountRequest;
import com.atm_transactionsystem.account_service.dto.DepositRequest;
import com.atm_transactionsystem.account_service.dto.WithdrawRequest;
import com.atm_transactionsystem.account_service.entity.Account;
import com.atm_transactionsystem.account_service.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountController {

    @Autowired
    private AccountService service;

    // Create Account
    @PostMapping
    public ResponseEntity<String> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        return new ResponseEntity<>(
                service.createAccount(request),
                HttpStatus.CREATED);
    }

    // Search Account
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> searchAccount(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                service.getAccount(accountNumber));
    }

    // Deposit
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @Valid @RequestBody DepositRequest request) {

        return ResponseEntity.ok(
                service.deposit(request));
    }

    // Withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @Valid @RequestBody WithdrawRequest request) {

        return ResponseEntity.ok(
                service.withdraw(request));
    }

    // Balance
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<Double> balance(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                service.checkBalance(accountNumber));
    }
}