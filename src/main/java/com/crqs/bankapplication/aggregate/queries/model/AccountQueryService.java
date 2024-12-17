package com.crqs.bankapplication.aggregate.queries.model;


import com.crqs.bankapplication.aggregate.queries.GetAccountBalance;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountQueryService {

    private final QueryGateway queryGateway;

    public AccountQueryService(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    public BigDecimal getDestinationAccountBalance(String destinationAccountId) {
        return queryGateway.query(
                new GetAccountBalance(destinationAccountId),
                BigDecimal.class
        ).join();
    }


}

