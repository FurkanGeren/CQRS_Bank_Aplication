package com.crqs.bankapplication.query.controller;


import com.crqs.bankapplication.common.commands.customer.LoginCustomerQuery;
import com.crqs.bankapplication.common.dto.LoginRequest;
import com.crqs.bankapplication.query.response.LoginResponse;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerQueryController {

    private final QueryGateway queryGateway;

    public CustomerQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }


    @PostMapping("/login")
    public CompletableFuture<LoginResponse> login(@RequestBody LoginRequest dto) {
        return queryGateway.query(new LoginCustomerQuery(
                dto.citizenID(), dto.password()), ResponseTypes.instanceOf(LoginResponse.class));
    }
}
