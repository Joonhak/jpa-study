package io.joonak.account.repository;

import io.joonak.account.domain.Account;
import io.joonak.account.dto.AccountSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountCustomRepository {

    Page<Account> search(AccountSearchType type, String value, Pageable pageable);

}
