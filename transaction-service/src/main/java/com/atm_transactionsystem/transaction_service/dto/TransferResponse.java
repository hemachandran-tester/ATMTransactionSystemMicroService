package com.atm_transactionsystem.transaction_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private String message;
    private Long transactionId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String transactionType;
    private double amount;
    private double balanceAfterTransaction;
    private String status;
    private LocalDateTime transactionDate;
}