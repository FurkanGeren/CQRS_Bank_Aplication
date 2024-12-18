package com.crqs.bankapplication.command.handler;

import com.crqs.bankapplication.common.events.AccountCreatedEvent;
import com.crqs.bankapplication.common.events.MoneyDepositedEvent;
import com.crqs.bankapplication.common.events.MoneySentEvent;
import com.crqs.bankapplication.common.events.MoneyWithdrawnEvent;
import com.crqs.bankapplication.common.model.Account;
import com.crqs.bankapplication.query.repository.AccountRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("account")
public class AccountEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccountEventHandler.class);

    private final AccountRepository accountRepository;

    public AccountEventHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        accountRepository.save(new Account(event.getAccountId(), event.getInitialBalance(), event.getUserFirstName(), event.getUserLastName()));
    }

    @EventHandler
    public void on(MoneyDepositedEvent event) {
        Account account = accountRepository.findById(event.getAccountId()).orElseThrow();
        logger.info("Event balance deposited: {}", event.getBalance());
        account.setBalance(account.getBalance().add(event.getAmount()));
        accountRepository.save(account);
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event) {
        Account account = accountRepository.findById(event.getAccountId()).orElseThrow();
        logger.info("Event balance withdrawn: {}", event.getBalance());
        account.setBalance(account.getBalance().subtract(event.getAmount()));
        accountRepository.save(account);
    }


}
