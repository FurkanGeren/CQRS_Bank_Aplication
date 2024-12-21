package com.crqs.bankapplication.query.service.account;

import com.crqs.bankapplication.common.commands.customer.LoginCustomerQuery;
import com.crqs.bankapplication.common.configuration.JwtService;
import com.crqs.bankapplication.common.enums.OperationType;
import com.crqs.bankapplication.common.events.MoneyDepositedEvent;
import com.crqs.bankapplication.common.events.MoneySentEvent;
import com.crqs.bankapplication.common.events.MoneyWithdrawnEvent;
import com.crqs.bankapplication.query.entity.Account;
import com.crqs.bankapplication.common.query.FindAccountHistoryQuery;
import com.crqs.bankapplication.common.query.FindAccountQuery;
import com.crqs.bankapplication.common.query.GetAccountBalanceQuery;
import com.crqs.bankapplication.query.entity.Customer;
import com.crqs.bankapplication.query.repository.AccountRepository;
import com.crqs.bankapplication.query.repository.CustomerRepository;
import com.crqs.bankapplication.query.response.AccountResponse;
import com.crqs.bankapplication.query.response.LoginResponse;
import com.crqs.bankapplication.query.response.TransactionResponse;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.DomainEventMessage;
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
public class AccountQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccountQueryHandler.class);

    private final AccountRepository accountRepository;
    private final EventStore eventStore;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    public AccountQueryHandler(AccountRepository accountRepository, EventStore eventStore, JwtService jwtService, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.eventStore = eventStore;
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
    }

    @QueryHandler
    public AccountResponse handle(FindAccountQuery event) {
        Account account = accountRepository.findById(event.getAccountId()).orElseThrow();
        return new AccountResponse(account.getAccountId(), account.getBalance());
    }


    @QueryHandler
    public List<TransactionResponse> handle(FindAccountHistoryQuery query) {
        List<TransactionResponse> transactionHistory = new ArrayList<>();

        String customerId = jwtService.extractUserName(query.getAccountId());
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        String accountId = customer.getAccount().getAccountId();
        System.out.println(accountId);


        List<DomainEventMessage<?>> events = eventStore.readEvents(accountId).asStream().collect(Collectors.toList());

        for (DomainEventMessage<?> event : events) {
            if (event.getPayload() instanceof MoneyDepositedEvent depositEvent) {

                Instant eventTimestamp = event.getTimestamp();
                LocalDateTime eventDateTime = LocalDateTime.ofInstant(eventTimestamp, ZoneId.systemDefault());
                String formattedTimestamp = eventDateTime.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss"));


                if (!depositEvent.getDescription().equals(OperationType.DEPOSIT.name())){
                    transactionHistory.add(new TransactionResponse(
                            "Para alma",
                            "+" + depositEvent.getAmount(),
                            formattedTimestamp,
                            depositEvent.getBalance()
                    ));
                }else {
                    transactionHistory.add(new TransactionResponse(
                            "DEPOSIT",
                            "+" + depositEvent.getAmount(),
                            formattedTimestamp,
                            depositEvent.getBalance()
                    ));
                }

            } else if (event.getPayload() instanceof MoneyWithdrawnEvent withdrawalEvent) {

                Instant eventTimestamp = event.getTimestamp();
                LocalDateTime eventDateTime = LocalDateTime.ofInstant(eventTimestamp, ZoneId.systemDefault());
                String formattedTimestamp = eventDateTime.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss"));

                if (!withdrawalEvent.getDescription().equals(OperationType.WITHDRAWAL.name())){
                    transactionHistory.add(new TransactionResponse(
                            "Para alma",
                            "-" + withdrawalEvent.getAmount(),
                            formattedTimestamp,
                            withdrawalEvent.getBalance()
                    ));
                }else {
                    transactionHistory.add(new TransactionResponse(
                            "WITHDRAWAL",
                            "-" + withdrawalEvent.getAmount(),
                            formattedTimestamp,
                            withdrawalEvent.getBalance()
                    ));
                }

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

            }
        }
        return transactionHistory;
    }

    @QueryHandler
    public BigDecimal handle(GetAccountBalanceQuery accountQuery){
        return accountRepository.findById(accountQuery.getAccountId()).orElseThrow().getBalance();
    }

}
