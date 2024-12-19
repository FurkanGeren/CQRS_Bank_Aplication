package com.crqs.bankapplication.query.repository;

import com.crqs.bankapplication.query.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByCitizenID(String citizenID);
}
