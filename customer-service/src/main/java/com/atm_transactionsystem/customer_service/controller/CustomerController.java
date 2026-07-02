package com.atm_transactionsystem.customer_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.atm_transactionsystem.customer_service.dto.CustomerRequest;
import com.atm_transactionsystem.customer_service.entity.Customer;
import com.atm_transactionsystem.customer_service.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        String response = customerService.createCustomer(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(
            @PathVariable Long id) {

        return ResponseEntity.ok(customerService.getCustomer(id));
    }
}