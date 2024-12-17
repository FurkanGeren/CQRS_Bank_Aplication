package com.crqs.bankapplication.aggregate.saga;

import com.crqs.bankapplication.aggregate.commands.DepositMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.TransferMoneyCommand;
import com.crqs.bankapplication.aggregate.commands.WithdrawMoneyCommand;
import com.crqs.bankapplication.aggregate.events.MoneyTransferFailedEvent;
import com.crqs.bankapplication.aggregate.events.MoneyTransferStartedEvent;
import com.crqs.bankapplication.aggregate.events.MoneyTransferredEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneyTransferSaga {


    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private static final Logger logger = LoggerFactory.getLogger(MoneyTransferSaga.class);

    @Autowired
    public MoneyTransferSaga(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @EventHandler
    public void handleMoneyTransferStarted(MoneyTransferStartedEvent event) {
        TransferMoneyCommand transferMoneyCommand = new TransferMoneyCommand(
                event.getSenderAccountId(),
                event.getReceiverAccountId(),
                event.getAmount()
        );
        logger.info("Money transfer started");
        commandGateway.send(transferMoneyCommand);

    }

    @EventHandler
    public void handleMoneyTransferredClaim(MoneyTransferredEvent event) {
        logger.info("Money transfred");
        // 2. Aşama: Alıcı hesaba para yatır
        DepositMoneyCommand depositMoneyCommand = new DepositMoneyCommand(
                event.getReceiverAccountId(),
                event.getAmount()
        );

        commandGateway.send(depositMoneyCommand);

        WithdrawMoneyCommand withdrawMoneyCommand = new WithdrawMoneyCommand(
                event.getSenderAccountId(),
                event.getAmount()
        );

        commandGateway.send(withdrawMoneyCommand);
    }
//
//    @EventHandler
//    public void handleMoneyTransferredGive(MoneyTransferredEvent event) {
//        logger.info("Money transfred");
//        // 2. Aşama: Alıcı hesaba para yatır
//        WithdrawMoneyCommand withdrawMoneyCommand = new WithdrawMoneyCommand(
//                event.getSenderAccountId(),
//                event.getAmount()
//        );
//        commandGateway.send(withdrawMoneyCommand);
//    }



    @EventHandler
    public void handleMoneyTransferFailed(MoneyTransferFailedEvent event) {
        WithdrawMoneyCommand withdrawMoneyCommand = new WithdrawMoneyCommand(
                event.getSenderAccountId(),
                event.getAmount()
        );
        commandGateway.send(withdrawMoneyCommand);
    }

}
