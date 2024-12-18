package com.crqs.bankapplication.command.service;

import com.crqs.bankapplication.common.commands.account.DepositMoneyCommand;
import com.crqs.bankapplication.common.commands.account.WithdrawMoneyCommand;
import com.crqs.bankapplication.common.dto.TransferMoneyRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final CommandGateway commandGateway;

    public TransferService(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void transferMoney(TransferMoneyRequest request) {
        String messageFrom = request.description() + "| transfer to: " + request.toAccount();
        commandGateway.sendAndWait(
                new WithdrawMoneyCommand(
                        request.fromAccount(),
                        request.amount(),
                        messageFrom
                )
        );

        String messageTo = request.description() + "| transfer from: " + request.fromAccount();
        commandGateway.sendAndWait(
                new DepositMoneyCommand(
                        request.toAccount(),
                        request.amount(),
                        messageTo
                )
        );
    }
}
