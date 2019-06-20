package io.joonak.account.service;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.entity.Account;
import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account create(AccountDto.SignUpRequest dto) {
        return accountRepository.save(dto.toEntity());
    }

    public Account updateAddress(Long id, AccountDto.UpdateAddressRequest dto) {
        final Account account = findById(id);
        account.updateAddress(dto);
        return account;
    }

    public Account findById(Long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(
                        () -> new AccountNotFoundException(id)
                );
    }

}
