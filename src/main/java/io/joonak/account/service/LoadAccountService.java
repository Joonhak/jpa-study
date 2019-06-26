package io.joonak.account.service;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.entity.Account;
import io.joonak.account.entity.Email;
import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadAccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String address) throws UsernameNotFoundException {
        var email = Email.builder().address(address).build();
        return accountRepository.findByEmail(email)
                .map(AccountDto.SecurityAccount::new)
                .orElseThrow(() -> {
                    System.out.println("USERNAME NOT FOUND EXCEPTION");
                    return new UsernameNotFoundException("Can not found user. username: " + address );
                });
    }

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

}
