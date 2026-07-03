package com.atm_transactionsystem.account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawResponse {

    private String message;
    private String accountNumber;
    private double withdrawnAmount;
    private double currentBalance;
}