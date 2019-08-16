package io.joonak.account;

import com.querydsl.core.types.Predicate;
import io.joonak.account.domain.*;
import io.joonak.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private QAccount qAccount = QAccount.account;

    @Test
    public void save() {
        var account = Account.builder()
                .email(Email.of("test@test.com"))
                .firstName("test")
                .lastName("account")
                .password(Password.builder().value("test").build())
                .address(
                        Address.builder().address("test state").detailAddres("test street").zipCode("12345").build()
                )
                .build();
        var result = accountRepository.save(account);

        assertNotNull(result.getId());
        assertTrue(LocalDateTime.now().isAfter(result.getCreatedAt()));
        assertTrue(LocalDateTime.now().isAfter(result.getUpdatedAt()));
    }

    @Test
    public void findByEmail() {
        var email = "test001@test.com";
        var account = accountRepository.findByEmail(Email.of(email)).get();
        assertEquals(account.getEmail().getAddress(), email);
    }

    @Test
    public void findById() {
        Long id = 1L;
        var account = accountRepository.findById(id).get();
        assertEquals(account.getId(), id);
    }

    @Test
    public void isExistedEmail() {
        var email = "test001@test.com";
        var result = accountRepository.existsByEmail(Email.of(email));
        assertTrue(result);
    }

    @Test
    public void findRecentlyRegistered() {
        List<Account> accounts = accountRepository.findCurrentlyRegistered(10);
        assertThat(accounts.size()).isLessThan(11);
    }

    @Test
    public void predicateExistsTest() {
        final Predicate emailPredicate = qAccount.email.eq(Email.of("test001@test.com"));

        var emailExists = accountRepository.exists(emailPredicate);

        assertTrue(emailExists);

        final Predicate namePredicate = qAccount.firstName.eq("java");

        var nameExists = accountRepository.exists(namePredicate);

        assertFalse(nameExists);
    }

    @Test
    public void predicateCountTest() {
        final Predicate emailPredicate = qAccount.email.address.like("test01%");

        var emailCount = accountRepository.count(emailPredicate);

        assertThat(emailCount).isGreaterThan(1);
    }

}
