package com.crqs.bankapplication.command.aggregate;

import com.crqs.bankapplication.common.commands.customer.CreateCustomerCommand;
import com.crqs.bankapplication.common.commands.customer.LoginCustomerQuery;
import com.crqs.bankapplication.common.enums.Sex;
import com.crqs.bankapplication.common.events.Customer.CustomerCreatedEvent;
import com.crqs.bankapplication.common.events.Customer.LoginCustomerEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private String customerId;
    private String firstName;
    private String lastName;
    private String citizenID;
    private Sex sex;
    private String password;

    public CustomerAggregate() {
    }

    private static final Logger logger = LoggerFactory.getLogger(CustomerAggregate.class);



    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand command) {
        logger.info("Command received: {}", command);
        AggregateLifecycle.apply(new CustomerCreatedEvent(
                command.getCustomerId(),
                command.getFirstName(),
                command.getLastName(),
                command.getCitizenID(),
                command.getSex(),
                command.getPassword()
        ));
    }

    @EventSourcingHandler
    public void on(CustomerCreatedEvent event) {
        logger.info("EventHandler triggered for event: {}", event);
        this.customerId = event.getCustomerId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.citizenID = event.getCitizenID();
        this.sex = event.getSex();
        this.password = event.getPassword();
    }




}
