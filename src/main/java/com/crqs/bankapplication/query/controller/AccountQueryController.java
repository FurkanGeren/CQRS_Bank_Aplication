package com.crqs.bankapplication.query.controller;

import com.crqs.bankapplication.common.commands.customer.LoginCustomerQuery;
import com.crqs.bankapplication.common.dto.LoginRequest;
import com.crqs.bankapplication.common.query.FindAccountHistoryQuery;
import com.crqs.bankapplication.common.query.FindAccountQuery;
import com.crqs.bankapplication.query.response.AccountResponse;
import com.crqs.bankapplication.query.response.LoginResponse;
import com.crqs.bankapplication.query.response.TransactionResponse;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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