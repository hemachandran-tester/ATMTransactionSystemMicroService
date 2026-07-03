package com.atm_transactionsystem.customer_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.atm_transactionsystem.customer_service.dto.CustomerRequest;
import com.atm_transactionsystem.customer_service.dto.CustomerResponse;
import com.atm_transactionsystem.customer_service.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        return new ResponseEntity<>(
                customerService.createCustomer(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                customerService.getCustomer(id)
        );
    }
}