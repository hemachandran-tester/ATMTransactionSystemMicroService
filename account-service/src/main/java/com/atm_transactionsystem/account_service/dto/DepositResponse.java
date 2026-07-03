package com.atm_transactionsystem.account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositResponse {

    private String message;
    private String accountNumber;
    private double depositedAmount;
    private double currentBalance;
}