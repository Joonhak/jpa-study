package io.joonak.account.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Can not found Account for id: " + id);
    }
}
