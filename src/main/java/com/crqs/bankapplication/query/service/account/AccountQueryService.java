package com.crqs.bankapplication.query.service.account;


import com.crqs.bankapplication.common.query.GetAccountBalanceQuery;
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
                new GetAccountBalanceQuery(destinationAccountId),
                BigDecimal.class
        ).join();
    }


}

