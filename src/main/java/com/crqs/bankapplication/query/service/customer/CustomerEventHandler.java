package com.crqs.bankapplication.query.service.customer;

import com.crqs.bankapplication.common.events.Customer.CustomerCreatedEvent;
import com.crqs.bankapplication.query.entity.Customer;
import com.crqs.bankapplication.query.repository.CustomerRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("customer")
public class CustomerEventHandler {

    private final CustomerRepository customerRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomerEventHandler.class);

    public CustomerEventHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @EventHandler
    public void on(CustomerCreatedEvent event) {
        logger.info("Customer created event: {}", event);
        customerRepository.save(new Customer(event.getCustomerId(),event.getFirstName(),event.getLastName(),event.getCitizenID(),event.getSex(), null));
    }

}
