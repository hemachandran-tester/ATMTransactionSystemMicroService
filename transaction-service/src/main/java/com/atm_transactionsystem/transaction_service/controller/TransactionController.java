package com.atm_transactionsystem.transaction_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.atm_transactionsystem.transaction_service.dto.DepositRequest;
import com.atm_transactionsystem.transaction_service.dto.TransferRequest;
import com.atm_transactionsystem.transaction_service.dto.WithdrawRequest;
import com.atm_transactionsystem.transaction_service.entity.Transaction;
import com.atm_transactionsystem.transaction_service.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @Valid @RequestBody DepositRequest request)
            throws Exception {

        return ResponseEntity.ok(
                service.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @Valid @RequestBody WithdrawRequest request)
            throws Exception {

        return ResponseEntity.ok(
                service.withdraw(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @Valid @RequestBody TransferRequest request)
            throws Exception {

        return ResponseEntity.ok(
                service.transfer(request));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<Transaction>> history(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                service.history(accountNumber));
    }
}