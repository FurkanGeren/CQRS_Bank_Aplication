package com.crqs.bankapplication.controller;

import com.crqs.bankapplication.aggregate.queries.FindAccountHistoryQuery;
import com.crqs.bankapplication.aggregate.queries.FindAccountQuery;
import com.crqs.bankapplication.response.AccountResponse;
import com.crqs.bankapplication.response.TransactionResponse;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountQueryController {

    private final QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    // Hesap detaylarını sorgula
    @GetMapping("/{accountId}")
    public CompletableFuture<AccountResponse> getAccountDetails(@PathVariable String accountId) {
        return queryGateway.query(new FindAccountQuery(accountId), AccountResponse.class);
    }

    // Hesap geçmişini sorgula
    @GetMapping("/{accountId}/history")
    public CompletableFuture<List<TransactionResponse>> getAccountHistory(@PathVariable String accountId) {
        return queryGateway.query(new FindAccountHistoryQuery(accountId), ResponseTypes.multipleInstancesOf(TransactionResponse.class));
    }
}