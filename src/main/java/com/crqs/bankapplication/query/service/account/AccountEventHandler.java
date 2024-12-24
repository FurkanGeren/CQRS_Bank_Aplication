package com.crqs.bankapplication.query.service.account;

import com.crqs.bankapplication.common.events.AccountCreatedEvent;
import com.crqs.bankapplication.common.events.MoneyDepositedEvent;
import com.crqs.bankapplication.common.events.MoneyWithdrawnEvent;
import com.crqs.bankapplication.common.exception.AccountNotFoundException;
import com.crqs.bankapplication.common.exception.CustomerAlreadyHaveAccountException;
import com.crqs.bankapplication.common.exception.CustomerNotFoundException;
import com.crqs.bankapplication.query.entity.Account;
import com.crqs.bankapplication.query.entity.Customer;
import com.crqs.bankapplication.query.repository.AccountRepository;
import com.crqs.bankapplication.query.repository.CustomerRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccountEventHandler.class);

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountEventHandler(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        Customer customer = customerRepository.findById(event.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        accountRepository.save(new Account(event.getAccountId(), event.getInitialBalance(), customer));
    }

    @EventHandler
    public void on(MoneyDepositedEvent event) {
        Account account = accountRepository.findById(event.getAccountId()).orElseThrow(() -> new AccountNotFoundException("Account not found"));
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
