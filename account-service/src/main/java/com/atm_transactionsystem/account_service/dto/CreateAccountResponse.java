package com.atm_transactionsystem.account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponse {

    private String message;
    private Long customerId;
    private String accountNumber;
    private String accountType;
    private double balance;
    private String status;
}