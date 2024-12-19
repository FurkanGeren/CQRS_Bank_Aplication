package com.crqs.bankapplication.query.service.customer;

import com.crqs.bankapplication.common.configuration.JwtService;
import com.crqs.bankapplication.common.events.AccountCreatedEvent;
import com.crqs.bankapplication.common.events.Customer.CustomerCreatedEvent;
import com.crqs.bankapplication.common.events.Customer.LoginCustomerEvent;
import com.crqs.bankapplication.query.entity.Account;
import com.crqs.bankapplication.query.entity.Customer;
import com.crqs.bankapplication.query.repository.CustomerRepository;
import com.crqs.bankapplication.query.response.LoginResponse;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerEventHandler {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerEventHandler.class);

    public CustomerEventHandler(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @EventHandler
    public void on(CustomerCreatedEvent event) {
        logger.info("Customer created event: {}", event);
        String password = passwordEncoder.encode(event.getPassword());

        customerRepository.save(new Customer(event.getCustomerId(),event.getFirstName(),event.getLastName(),event.getCitizenID(), password, event.getSex(), null));
    }

    @EventHandler
    public LoginResponse on(LoginCustomerEvent event) {
        Customer customer = customerRepository.findById(event.getCitizenID())
                .orElseThrow();

        if(!passwordEncoder.matches(event.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtService.generateToken(customer.getId());
        return new LoginResponse(token);
    }


}
