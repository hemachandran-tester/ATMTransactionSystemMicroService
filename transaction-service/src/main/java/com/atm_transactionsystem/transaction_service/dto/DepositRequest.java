package com.atm_transactionsystem.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DepositRequest {

    private String accountNumber;

    private double amount;

    // getters setters

}