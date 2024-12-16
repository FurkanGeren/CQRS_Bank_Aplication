package com.crqs.bankapplication.aggregate;

import com.crqs.bankapplication.aggregate.commands.CreateAccountCommand;
import com.crqs.bankapplication.aggregate.commands.DepositMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.TransferMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.WithdrawMoneyCommand;
import com.crqs.bankapplication.aggregate.events.*;
import com.crqs.bankapplication.aggregate.queries.FindAccountQuery;
import com.crqs.bankapplication.aggregate.queries.model.AccountEventHandler;
import com.crqs.bankapplication.aggregate.queries.model.AccountQueryService;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.queryhandling.QueryGateway;
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

    @CommandHandler
    public void handle(DepositMoneyCommand command){
        validateAmount(command.getAmount());

        this.balance = this.balance.add(command.getAmount());
        AggregateLifecycle.apply(new MoneyDepositedEvent(command.getAccountId(), command.getAmount(), this.balance));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command) {
        validateAmount(command.getAmount());

        if (balance.compareTo(command.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        this.balance = this.balance.subtract(command.getAmount());

        AggregateLifecycle.apply(new MoneyWithdrawnEvent(command.getAccountId(), command.getAmount(), this.balance));
    }

    @CommandHandler
    public void handle(TransferMoneyCommand command) {
        validateAmount(command.getAmount());
        if (balance.compareTo(command.getAmount()) < 0) {
            throw new IllegalArgumentException("Yetersiz bakiye.");
        }
        logger.info("balance: {}", this.balance);
        logger.info("amount: {}", command.getAmount());


        this.balance = this.balance.subtract(command.getAmount());

        logger.info("balance after subtract: {}", this.balance);

        AggregateLifecycle.apply(new MoneySentEvent(
                command.getSourceAccountId(),
                command.getDestinationAccountId(),
                command.getAmount(),
                this.balance
        ));
        BigDecimal destinationBalance = accountQueryService.getDestinationAccountBalance(command.getDestinationAccountId());

        BigDecimal destinationBalanceAfterTransfer = destinationBalance.add(command.getAmount());

        logger.info("destinationBalance: {}", destinationBalance);

        logger.info("destinationBalanceAfterTransfer: {}", destinationBalanceAfterTransfer);

        AggregateLifecycle.apply(new MoneyReceivedEvent(
                command.getDestinationAccountId(),
                command.getSourceAccountId(),
                command.getAmount(),
                destinationBalanceAfterTransfer
        ));
    }

    @EventSourcingHandler
    public void on(MoneySentEvent event) {
        //this.balance = this.balance.add(event.getAmount());
        logger.info("MoneySentEvent applied, new balance for source account: {}", event.getSourceBalance());
        this.balance = event.getSourceBalance();
    }

//    @EventSourcingHandler
//    public void on(MoneyReceivedEvent event) {
//        //this.balance = this.balance.subtract(event.getAmount());
//        logger.info("MoneyReceivedEvent applied, new balance for destination account: {}", event.getDestinationBalance());
//        this.balance = event.getDestinationBalance();
//    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId = event.getAccountId();
        this.balance = event.getInitialBalance();
        this.userFirstName = event.getUserFirstName();
        this.userLastName = event.getUserLastName();
    }

    @EventSourcingHandler
    public void on(MoneyDepositedEvent event) {
        logger.info("MoneyDepositedEvent applied, new balance for destination account: {}", event.getAmount());
        this.balance = this.balance.add(event.getAmount());
    }

    @EventSourcingHandler
    public void on(MoneyWithdrawnEvent event) {
        this.balance = this.balance.subtract(event.getAmount());
    }

    private void validateAmount(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
}
