package io.joonak.account.repository;

import io.joonak.account.domain.Account;
import io.joonak.account.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountCustomRepository, QuerydslPredicateExecutor<Account> {

    boolean existsByEmail(Email email);

    Optional<Account> findByEmail(Email email);
    
}
