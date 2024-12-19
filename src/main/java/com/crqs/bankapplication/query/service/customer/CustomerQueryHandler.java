package com.crqs.bankapplication.query.service.customer;

import com.crqs.bankapplication.common.commands.customer.LoginCustomerQuery;
import com.crqs.bankapplication.common.configuration.JwtService;
import com.crqs.bankapplication.query.entity.Customer;
import com.crqs.bankapplication.query.repository.CustomerRepository;
import com.crqs.bankapplication.query.response.LoginResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerQueryHandler {


    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public CustomerQueryHandler(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @QueryHandler
    public LoginResponse handle(LoginCustomerQuery query){
        Customer customer = customerRepository.findById(query.getCitizenID())
                .orElseThrow();

        if(!passwordEncoder.matches(query.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtService.generateToken(customer.getId());
        return new LoginResponse(token);
    }
}
