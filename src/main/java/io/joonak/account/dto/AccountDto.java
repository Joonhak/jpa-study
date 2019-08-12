package io.joonak.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.joonak.account.domain.Account;
import io.joonak.account.domain.Address;
import io.joonak.account.domain.Email;
import io.joonak.account.domain.Password;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class AccountDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequest {
        @Valid
        private Email email;
        @NotEmpty
        private String firstName;
        @NotEmpty
        private String lastName;
        @NotEmpty
        private String password;
        @Valid
        private Address address;

        @Builder
        public SignUpRequest(Email email, String firstName, String lastName, String password, Address address) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.address = address;
        }

        public Account toEntity() {
            return Account.builder()
                    .email(this.email)
                    .firstName(this.firstName)
                    .lastName(this.lastName)
                    .password(Password.builder().value(this.password).build())
                    .address(this.address)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateAddressRequest {
        @Valid
        private Address address;

        @Builder
        public UpdateAddressRequest(Address address) {
            this.address = address;
        }
    }

    @Getter
    public static class Response {
        private String email;
        private String firstName;
        private String lastName;
        private Address address;

        public Response(Account account) {
            this.email = account.getEmail().getAddress();
            this.firstName = account.getFirstName();
            this.lastName = account.getLastName();
            this.address = account.getAddress();
        }
    }

}
