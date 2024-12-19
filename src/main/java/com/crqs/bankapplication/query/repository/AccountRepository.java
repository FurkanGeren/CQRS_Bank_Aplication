package com.crqs.bankapplication.query.repository;

import com.crqs.bankapplication.query.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String > {
    Optional<Account> findByCustomerId(String customerId);
}
