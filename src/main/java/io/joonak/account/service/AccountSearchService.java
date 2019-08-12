package io.joonak.account.service;

import io.joonak.account.domain.Account;
import io.joonak.account.dto.AccountSearchType;
import io.joonak.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountSearchService {

    private final AccountRepository accountRepository;

    public Page<Account> search(AccountSearchType type, String value, PageRequest of) {
        return accountRepository.search(type, value, of);
    }

}
