package io.joonak.account.service;

import io.joonak.account.domain.Account;
import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadAccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

}
