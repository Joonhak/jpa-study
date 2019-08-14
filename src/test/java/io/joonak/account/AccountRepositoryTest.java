package io.joonak.account;

import io.joonak.account.domain.Account;
import io.joonak.account.domain.Email;
import io.joonak.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findByEmail() {
        var email = "test001@test.com";
        var account = accountRepository.findByEmail(Email.of(email)).get();
        assertThat(account.getEmail().getAddress()).isEqualTo(email);
    }

    @Test
    public void findById() {
        var id = 1L;
        var account = accountRepository.findById(id).get();
        assertThat(account.getId()).isEqualTo(id);
    }

    @Test
    public void isExistedEmail() {
        var email = "test001@test.com";
        var result = accountRepository.existsByEmail(Email.of(email));
        assertThat(result).isTrue();
    }

    @Test
    public void findRecentlyRegistered() {
        List<Account> accounts = accountRepository.findCurrentlyRegistered(10);
        assertThat(accounts.size()).isLessThan(11);
    }

}
