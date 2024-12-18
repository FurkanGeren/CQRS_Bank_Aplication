package com.crqs.bankapplication.command.controller;

import com.crqs.bankapplication.command.service.TransferService;
import com.crqs.bankapplication.common.commands.account.CreateAccountCommand;
import com.crqs.bankapplication.common.commands.account.DepositMoneyCommand;
import com.crqs.bankapplication.common.commands.account.WithdrawMoneyCommand;
import com.crqs.bankapplication.common.dto.*;
import com.crqs.bankapplication.common.enums.OperationType;
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
    private final TransferService transferService;

    private static final Logger logger = LoggerFactory.getLogger(AccountCommandController.class);


    public AccountCommandController(CommandGateway commandGateway, TransferService transferService) {
        this.commandGateway = commandGateway;
        this.transferService = transferService;
    }


    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest request) {
        String accountId = UUID.randomUUID().toString();
        BigDecimal bigDecimal = new BigDecimal("0.0");
        CreateAccountCommand command = new CreateAccountCommand(accountId, bigDecimal, request.customerId());
        commandGateway.sendAndWait(command);
        return ResponseEntity.ok("Account created with ID: " + accountId);
    }



    @PostMapping("/deposit")
    public CompletableFuture<String> depositMoney(@RequestBody DepositMoneyRequest request) {
        return commandGateway.send(
                new DepositMoneyCommand(
                        request.accountId(),
                        request.amount(),
                        OperationType.DEPOSIT.name()
                )
        );
    }

    @PostMapping("/withdraw")
    private CompletableFuture<String> withdrawMoney(@RequestBody WithdrawMoneyRequest request) {

        return commandGateway.send(
                new WithdrawMoneyCommand(
                        request.accountId(),
                        request.amount(),
                        OperationType.WITHDRAWAL.name()
                )
        );
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferMoneyRequest request) {
        transferService.transferMoney(request);
        return ResponseEntity.ok("Transfer completed successfully.");
    }


}