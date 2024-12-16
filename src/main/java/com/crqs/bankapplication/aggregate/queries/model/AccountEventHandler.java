package com.crqs.bankapplication.aggregate.queries.model;

import com.crqs.bankapplication.aggregate.AccountAggregate;
import com.crqs.bankapplication.aggregate.events.*;
import com.crqs.bankapplication.aggregate.queries.FindAccountHistoryQuery;
import com.crqs.bankapplication.aggregate.queries.FindAccountQuery;
import com.crqs.bankapplication.aggregate.queries.GetAccountBalance;
import com.crqs.bankapplication.model.Account;
import com.crqs.bankapplication.repository.AccountRepository;
import com.crqs.bankapplication.response.AccountResponse;
import com.crqs.bankapplication.response.TransactionResponse;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ProcessingGroup("account")
public class AccountEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccountEventHandler.class);

    private final AccountRepository accountRepository;
    private final EventStore eventStore;

    public AccountEventHandler(AccountRepository accountRepository, EventStore eventStore) {
        this.accountRepository = accountRepository;
        this.eventStore = eventStore;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        accountRepository.save(new Account(event.getAccountId(), event.getInitialBalance(), event.getUserFirstName(), event.getUserLastName()));
    }

    @EventHandler
    public void on(MoneyDepositedEvent event) {
        Account account = accountRepository.findById(event.getAccountId()).orElseThrow();
        logger.info("Event balance deposited: {}", event.getBalance());
        account.setBalance(account.getBalance().add(event.getAmount()));
        accountRepository.save(account);
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event) {
        Account account = accountRepository.findById(event.getAccountId()).orElseThrow();
        logger.info("Event balance withdrawn: {}", event.getBalance());
        account.setBalance(account.getBalance().subtract(event.getAmount()));
        accountRepository.save(account);
    }

    @EventHandler
    public void on(MoneySentEvent event) {
        Account sourceAccount = accountRepository.findById(event.getSourceAccountId()).orElseThrow();
        logger.info("Event balance sent: {}", event.getSourceBalance());
        sourceAccount.setBalance(event.getSourceBalance());
        accountRepository.save(sourceAccount);
    }

    @EventHandler
    public void on(MoneyReceivedEvent event) {
        Account destinationAccount = accountRepository.findById(event.getDestinationAccountId()).orElseThrow();
        logger.info("Event balance received: {}", event.getDestinationBalance());
        destinationAccount.setBalance(event.getDestinationBalance());
        accountRepository.save(destinationAccount);
    }

    @QueryHandler
    public AccountResponse handle(FindAccountQuery event) {
        Account account = accountRepository.findById(event.getAccountId()).orElseThrow();
        return new AccountResponse(account.getAccountId(), account.getBalance());
    }


    @QueryHandler
    public List< TransactionResponse> handle(FindAccountHistoryQuery query) {
        List<TransactionResponse> transactionHistory = new ArrayList<>();

        List<DomainEventMessage<?>> events = eventStore.readEvents(query.getAccountId()).asStream().collect(Collectors.toList());

        for (DomainEventMessage<?> event : events) {
            if (event.getPayload() instanceof MoneyDepositedEvent depositEvent) {

                Instant eventTimestamp = event.getTimestamp();
                LocalDateTime eventDateTime = LocalDateTime.ofInstant(eventTimestamp, ZoneId.systemDefault());
                String formattedTimestamp = eventDateTime.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss"));

                transactionHistory.add(new TransactionResponse(
                        "DEPOSIT",
                        "+" + depositEvent.getAmount(),
                        formattedTimestamp,
                        depositEvent.getBalance()
                ));
            } else if (event.getPayload() instanceof MoneyWithdrawnEvent withdrawalEvent) {

                Instant eventTimestamp = event.getTimestamp();
                LocalDateTime eventDateTime = LocalDateTime.ofInstant(eventTimestamp, ZoneId.systemDefault());
                String formattedTimestamp = eventDateTime.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss"));

                transactionHistory.add(new TransactionResponse(
                        "WITHDRAWAL",
                        "-" + withdrawalEvent.getAmount(),
                        formattedTimestamp,
                        withdrawalEvent.getBalance()
                ));
            } else if(event.getPayload() instanceof MoneySentEvent sentEvent) {
                Instant eventTimestamp = event.getTimestamp();
                LocalDateTime eventDateTime = LocalDateTime.ofInstant(eventTimestamp, ZoneId.systemDefault());
                String formattedTimestamp = eventDateTime.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss"));

                transactionHistory.add(new TransactionResponse(
                        "TRANSFER",
                        "-" + sentEvent.getAmount(),
                        formattedTimestamp,
                        sentEvent.getSourceBalance()
                ));
            } else if (event.getPayload() instanceof MoneyReceivedEvent receivedEvent) {
                Instant eventTimestamp = event.getTimestamp();
                LocalDateTime eventDateTime = LocalDateTime.ofInstant(eventTimestamp, ZoneId.systemDefault());
                String formattedTimestamp = eventDateTime.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss"));

                transactionHistory.add(new TransactionResponse(
                        "TRANSFER",
                        "+" + receivedEvent.getAmount(),
                        formattedTimestamp,
                        receivedEvent.getDestinationBalance()
                ));
            }
        }
        return transactionHistory;
    }

    @QueryHandler
    public BigDecimal handle(GetAccountBalance accountQuery){
        return accountRepository.findById(accountQuery.getAccountId()).orElseThrow().getBalance();
    }

}
