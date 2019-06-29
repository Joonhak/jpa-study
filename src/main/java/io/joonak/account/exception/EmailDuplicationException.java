package io.joonak.account.exception;

import io.joonak.account.domain.Email;
import lombok.Getter;

@Getter
public class EmailDuplicationException extends RuntimeException {

    private Email email;
    private String field;

    public EmailDuplicationException(Email email) {
        this.email = email;
        this.field = "email";
    }
}
