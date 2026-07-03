package com.atm_transactionsystem.customer_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm_transactionsystem.customer_service.dto.CustomerRequest;
import com.atm_transactionsystem.customer_service.dto.CustomerResponse;
import com.atm_transactionsystem.customer_service.entity.Customer;
import com.atm_transactionsystem.customer_service.exception.CustomerAlreadyExistsException;
import com.atm_transactionsystem.customer_service.exception.CustomerNotFoundException;
import com.atm_transactionsystem.customer_service.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public CustomerResponse createCustomer(CustomerRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException(
                    "Customer already exists.");
        }

        Customer customer = new Customer();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer savedCustomer = repository.save(customer);

        return mapToResponse(
                savedCustomer,
                "Customer Created Successfully"
        );
    }

    public CustomerResponse getCustomer(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer Not Found"));

        return mapToResponse(
                customer,
                "Customer Retrieved Successfully"
        );
    }

    private CustomerResponse mapToResponse(Customer customer, String message) {

        return new CustomerResponse(
                message,
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress()
        );
    }
}