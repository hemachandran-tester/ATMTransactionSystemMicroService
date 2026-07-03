package com.atm_transactionsystem.customer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private String message;
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}