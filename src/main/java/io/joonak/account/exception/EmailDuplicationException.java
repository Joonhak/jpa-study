package io.joonak.account.exception;

import lombok.Getter;

@Getter
public class EmailDuplicationException extends RuntimeException {

    private String email;
    private String field;

    public EmailDuplicationException(String email) {
        this.email = email;
        this.field = "email";
    }
}
