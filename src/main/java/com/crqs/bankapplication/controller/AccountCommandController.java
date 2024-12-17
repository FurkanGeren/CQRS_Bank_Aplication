package com.crqs.bankapplication.controller;

import com.crqs.bankapplication.aggregate.commands.CreateAccountCommand;
import com.crqs.bankapplication.aggregate.commands.DepositMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.WithdrawMoneyCommand;
import com.crqs.bankapplication.dto.CreateAccountRequest;
import com.crqs.bankapplication.dto.DepositMoneyRequest;
import com.crqs.bankapplication.dto.TransferMoneyRequest;
import com.crqs.bankapplication.dto.WithdrawMoneyRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

//    // Para yatırma
//    @PostMapping("/{accountId}/deposit")
//    public ResponseEntity<String> depositMoney(@PathVariable String accountId, @RequestBody DepositMoneyRequest request) {
//        DepositMoneyCommand command = new DepositMoneyCommand(accountId, request.getAmount());
//        commandGateway.sendAndWait(command);
//        return ResponseEntity.ok("Money deposited successfully!");
//    }

    @PostMapping("/deposit")
    public CompletableFuture<String> depositMoney(@RequestBody DepositMoneyRequest request) {
//        DepositMoneyCommand command = new DepositMoneyCommand(request.getAmount());
//        commandGateway.sendAndWait(command);
        return commandGateway.send(
                new DepositMoneyCommand(
                        request.accountId(),
                        request.amount(),
                        request.description()
                )
        );
    }

    // Para çekme
    @PostMapping("/withdraw")
    public CompletableFuture<String> withdrawMoney(@RequestBody WithdrawMoneyRequest request) {
//        WithdrawMoneyCommand command = new WithdrawMoneyCommand(accountId, request.getAmount());
//        commandGateway.sendAndWait(command);
//        return ResponseEntity.ok("Money withdrawn successfully!");
        return commandGateway.send(
                new WithdrawMoneyCommand(
                        request.accountId(),
                        request.amount(),
                        request.description()
                )
        );
    }

    @PostMapping("/transfer")
    public List<CompletableFuture<String>> transfer(@RequestBody TransferMoneyRequest request) {
//        commandGateway.send(new TransferMoneyCommand(request.getSourceAccountId(),
//                request.getDestinationAccountId(),
//                request.getAmount()));
        List<CompletableFuture<String>> completableFutures = new ArrayList<>();
        String messageFrom = request.description() + "| transfer to:" + request.toAccount();

        CompletableFuture<String> withdraw = withdrawMoney(new WithdrawMoneyRequest(
                request.fromAccount(), messageFrom, request.amount()
                )
        );
        withdraw.join();
        completableFutures.add(withdraw);

        String messageTo = request.description() + "| transfer from:" + request.fromAccount();
        CompletableFuture<String> deposit = depositMoney(new DepositMoneyRequest(
                request.toAccount(), messageTo, request.amount()
        ));

        deposit.join();
        completableFutures.add(deposit);
        return completableFutures;
    }


}