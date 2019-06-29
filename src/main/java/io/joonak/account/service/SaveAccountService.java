package io.joonak.account.service;

import io.joonak.account.domain.Account;
import io.joonak.account.domain.Email;
import io.joonak.account.dto.AccountDto;
import io.joonak.account.exception.EmailDuplicationException;
import io.joonak.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SaveAccountService {

    private final AccountRepository accountRepository;

    public Account create(AccountDto.SignUpRequest dto) {
        if (isExistedEmail(dto.getEmail()))
            throw new EmailDuplicationException(dto.getEmail());
        return accountRepository.save(dto.toEntity());
    }

    @Transactional(readOnly = true)
    public boolean isExistedEmail(Email email) {
        return accountRepository.existsByEmail(email);
    }

}
