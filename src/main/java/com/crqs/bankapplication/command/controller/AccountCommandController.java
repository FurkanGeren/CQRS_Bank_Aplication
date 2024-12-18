package com.crqs.bankapplication.command.controller;

import com.crqs.bankapplication.common.commands.CreateAccountCommand;
import com.crqs.bankapplication.common.commands.DepositMoneyCommand;
import com.crqs.bankapplication.common.commands.WithdrawMoneyCommand;
import com.crqs.bankapplication.common.dto.CreateAccountRequest;
import com.crqs.bankapplication.common.dto.DepositMoneyRequest;
import com.crqs.bankapplication.common.dto.TransferMoneyRequest;
import com.crqs.bankapplication.common.dto.WithdrawMoneyRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountCommandController {

    private final CommandGateway commandGateway;

    private static final Logger logger = LoggerFactory.getLogger(AccountCommandController.class);


    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }


    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest request) {
        String accountId = UUID.randomUUID().toString();
        BigDecimal bigDecimal = new BigDecimal("0.0");
        CreateAccountCommand command = new CreateAccountCommand(accountId, bigDecimal, request.firstName(), request.lastName());
        commandGateway.sendAndWait(command);
        return ResponseEntity.ok("Account created with ID: " + accountId);
    }



    @PostMapping("/deposit")
    public CompletableFuture<String> depositMoney(@RequestBody DepositMoneyRequest request) {
        return commandGateway.send(
                new DepositMoneyCommand(
                        request.accountId(),
                        request.amount(),
                        request.description()
                )
        );
    }

    @PostMapping("/withdraw")
    public CompletableFuture<String> withdrawMoney(@RequestBody WithdrawMoneyRequest request) {
        return commandGateway.send(
                new WithdrawMoneyCommand(
                        request.accountId(),
                        request.amount(),
                        request.description()
                )
        );
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferMoneyRequest request) {
        // Withdraw amount from sender account
        String messageFrom = request.description() + "| transfer to:" + request.toAccount();
        CompletableFuture<String> withdraw = withdrawMoney(new WithdrawMoneyRequest(
                request.fromAccount(), messageFrom, request.amount()
        ));
        withdraw.join();

        // Deposit amount to receiver account
        String messageTo = request.description() + "| transfer from:" + request.fromAccount();
        CompletableFuture<String> deposit = depositMoney(new DepositMoneyRequest(
                request.toAccount(), messageTo, request.amount()
        ));
        deposit.join();
        return ResponseEntity.ok("Transfer completed successfully.");
    }


}