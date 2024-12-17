package com.crqs.bankapplication.aggregate.queries.model;

import com.crqs.bankapplication.aggregate.commands.DepositMoneyCommand;
import com.crqs.bankapplication.aggregate.queries.FindAccountQuery;
import com.crqs.bankapplication.aggregate.queries.GetAccountBalance;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class AccountQueryService {

    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    public AccountQueryService(QueryGateway queryGateway, CommandGateway commandGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }

    // Alıcı hesabının bakiyesini sorgulayan metot
    public BigDecimal getDestinationAccountBalance(String destinationAccountId) {
        return queryGateway.query(
                new GetAccountBalance(destinationAccountId),
                BigDecimal.class
        ).join();
    }


}

