package com.crqs.bankapplication.aggregate;

import com.crqs.bankapplication.aggregate.commands.CreateAccountCommand;
import com.crqs.bankapplication.aggregate.commands.DepositMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.TransferMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.WithdrawMoneyCommand;
import com.crqs.bankapplication.aggregate.events.*;
import com.crqs.bankapplication.aggregate.queries.model.AccountQueryService;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private BigDecimal balance;
    private String userFirstName;
    private String userLastName;

    private static final Logger logger = LoggerFactory.getLogger(AccountAggregate.class);


    @Autowired
    private AccountQueryService accountQueryService;

    public AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command){
        validateAmount(command.getInitialBalance());

        AggregateLifecycle.apply(new AccountCreatedEvent(command.getAccountId(), command.getInitialBalance(), command.getUserFirstName(), command.getUserLastName()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId = event.getAccountId();
        this.balance = event.getInitialBalance();
        this.userFirstName = event.getUserFirstName();
        this.userLastName = event.getUserLastName();
    }


    @CommandHandler
    public void handle(DepositMoneyCommand command){
        logger.info("DepositMoneyCommand handled");

        this.balance = this.balance.add(command.getAmount());
        logger.info("DepositMoneyCommand handled balance: {}", this.balance);

        AggregateLifecycle.apply(new MoneyDepositedEvent(
                command.getAccountId(),
                command.getAmount(),
                this.balance,
                command.getDescription()
        ));
    }

    @EventSourcingHandler
    public void on(MoneyDepositedEvent event) {
        logger.info("MoneyDepositedEvent handled");
        this.accountId = event.getAccountId();
        this.balance = event.getBalance();
    }


    @CommandHandler
    public void handle(WithdrawMoneyCommand command) {
        logger.info("WithdrawMoneyCommand handled");
        if (this.balance.compareTo(BigDecimal.ZERO) > 0 && this.balance.compareTo(command.getAmount()) < 0) {
            throw new IllegalArgumentException("Balance not sufficient => " + this.balance);
        } else {
            this.balance = this.balance.subtract(command.getAmount());
            logger.info("WithdrawMoneyCommand handled balance: {}", this.balance);
            AggregateLifecycle.apply(new MoneyWithdrawnEvent(
                    command.getAccountId(),
                    command.getAmount(),
                    this.balance,
                    command.getDescription()
            ));
        }

    }

    @EventSourcingHandler
    public void on(MoneyWithdrawnEvent event) {
       logger.info("MoneyWithdrawnEvent handled");
       this.accountId = event.getAccountId();
       this.balance = event.getBalance();
       logger.info("MoneyWithdrawnEvent handled balance: {}", this.balance);
    }



    private void validateAmount(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
}
