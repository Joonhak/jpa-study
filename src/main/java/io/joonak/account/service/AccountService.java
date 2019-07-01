package io.joonak.account.service;

import io.joonak.account.domain.Account;
import io.joonak.account.domain.Email;
import io.joonak.account.dto.AccountDto;
import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.exception.EmailDuplicationException;
import io.joonak.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account create(AccountDto.SignUpRequest dto) {
        if (isExistedEmail(dto.getEmail()))
            throw new EmailDuplicationException(dto.getEmail());
        return accountRepository.save(dto.toEntity());
    }

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account findByEmail(Email email) {
        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(email));
    }

    @Transactional(readOnly = true)
    public boolean isExistedEmail(Email email) {
        return accountRepository.existsByEmail(email);
    }

    public Account updateAddress(Long id, AccountDto.UpdateAddressRequest dto) {
        final Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        account.updateAddress(dto);
        return account;
    }

}
