package io.joonak.account.service;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.domain.Account;
import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAccountService {

    private final AccountRepository accountRepository;

    public Account updateAddress(Long id, AccountDto.UpdateAddressRequest dto) {
        final Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        account.updateAddress(dto);
        System.out.println(account);
        return account;
    }

}
