package com.crqs.bankapplication.command.service;

import com.crqs.bankapplication.common.dto.CreateAccountRequest;
import com.crqs.bankapplication.common.exception.CustomerAlreadyHaveAccountException;
import com.crqs.bankapplication.common.exception.CustomerNotFoundException;
import com.crqs.bankapplication.query.entity.Account;
import com.crqs.bankapplication.query.entity.Customer;
import com.crqs.bankapplication.query.repository.AccountRepository;
import com.crqs.bankapplication.query.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountValidationService {

    private final CustomerRepository customerRepository;

    public AccountValidationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public void validateAccount(CreateAccountRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        if(customer.getAccount() != null) {
            throw new CustomerAlreadyHaveAccountException("This customer already has an account");
        }
    }


}
