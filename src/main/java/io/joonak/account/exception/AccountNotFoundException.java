package io.joonak.account.exception;

import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {

    private Long id;

    public AccountNotFoundException(Long id) {
        this.id = id;
    }

}
