package com.crqs.bankapplication.repository;

import com.crqs.bankapplication.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String > {
}
