package com.atm_transactionsystem.customer_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm_transactionsystem.customer_service.dto.CustomerRequest;
import com.atm_transactionsystem.customer_service.entity.Customer;
import com.atm_transactionsystem.customer_service.exception.CustomerAlreadyExistsException;
import com.atm_transactionsystem.customer_service.repository.CustomerRepository;
import com.atm_transactionsystem.customer_service.exception.CustomerNotFoundException;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public String createCustomer(CustomerRequest request) {

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

        repository.save(customer);

        return "Customer Created Successfully";
    }
    public Customer getCustomer(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer Not Found"));

    }

}