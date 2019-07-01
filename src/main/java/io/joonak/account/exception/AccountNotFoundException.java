package io.joonak.account.exception;

import io.joonak.account.domain.Email;
import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {
    private Long id;
    private Email email;

    public AccountNotFoundException(Long id) {
        super("Can not found account for id: " + id);
        this.id = id;
    }

    public AccountNotFoundException(Email email) {
        super("Can not found account for id: " + email.getAddress());
        this.email = email;
    }
}
