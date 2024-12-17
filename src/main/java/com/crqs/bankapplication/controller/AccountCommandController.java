package com.crqs.bankapplication.controller;

import com.crqs.bankapplication.aggregate.commands.CreateAccountCommand;
import com.crqs.bankapplication.aggregate.commands.DepositMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.TransferMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.WithdrawMoneyCommand;
import com.crqs.bankapplication.aggregate.events.MoneyTransferRequestedEvent;
import com.crqs.bankapplication.dto.CreateAccountRequest;
import com.crqs.bankapplication.dto.DepositMoneyRequest;
import com.crqs.bankapplication.dto.TransferMoneyRequest;
import com.crqs.bankapplication.dto.WithdrawMoneyRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountCommandController {

    private final CommandGateway commandGateway;

    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    // Hesap oluşturma
    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest request) {
        String accountId = UUID.randomUUID().toString();
        BigDecimal bigDecimal = new BigDecimal("0.0");
        CreateAccountCommand command = new CreateAccountCommand(accountId, bigDecimal, request.getFirstName(), request.getLastName());
        commandGateway.sendAndWait(command);
        return ResponseEntity.ok("Account created with ID: " + accountId);
    }

    // Para yatırma
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> depositMoney(@PathVariable String accountId, @RequestBody DepositMoneyRequest request) {
        DepositMoneyCommand command = new DepositMoneyCommand(accountId, request.getAmount());
        commandGateway.sendAndWait(command);
        return ResponseEntity.ok("Money deposited successfully!");
    }

    // Para çekme
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdrawMoney(@PathVariable String accountId, @RequestBody WithdrawMoneyRequest request) {
        WithdrawMoneyCommand command = new WithdrawMoneyCommand(accountId, request.getAmount());
        commandGateway.sendAndWait(command);
        return ResponseEntity.ok("Money withdrawn successfully!");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferMoneyRequest request) {
        commandGateway.send(new TransferMoneyCommand(request.getSourceAccountId(),
                request.getDestinationAccountId(),
                request.getAmount()));

        return ResponseEntity.ok("Money transfer successfully!");
    }


}