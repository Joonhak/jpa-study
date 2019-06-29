package io.joonak.account.dto;

import io.joonak.account.domain.Account;
import io.joonak.account.domain.Email;
import io.joonak.account.domain.Password;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

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
        @NotEmpty
        private String address;
        @NotEmpty
        private String detailAddress;
        @NotEmpty
        private String zipCode;

        @Builder
        public SignUpRequest(Email email, String firstName, String lastName, String password, String address, String detailAddress, String zipCode) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.address = address;
            this.detailAddress = detailAddress;
            this.zipCode = zipCode;
        }

        public Account toEntity() {
            return Account.builder()
                    .email(this.email)
                    .firstName(this.firstName)
                    .lastName(this.lastName)
                    .password(Password.builder().value(this.password).build())
                    .address(this.address)
                    .detailAddress(this.detailAddress)
                    .zipCode(this.zipCode)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateAddressRequest {
        private String address;
        private String detailAddress;
        private String zipCode;

        @Builder
        public UpdateAddressRequest(String address, String detailAddress, String zipCode) {
            this.address = address;
            this.detailAddress = detailAddress;
            this.zipCode = zipCode;
        }
    }

    @Getter
    public static class Response {
        private String email;
        private String firstName;
        private String lastName;
        private String address;
        private String detailAddress;
        private String zipCode;
        private LocalDateTime updatedAt;
        private LocalDateTime createdAt;

        public Response(Account account) {
            this.email = account.getEmail().getAddress();
            this.firstName = account.getFirstName();
            this.lastName = account.getLastName();
            this.address = account.getAddress();
            this.detailAddress = account.getDetailAddress();
            this.zipCode = account.getZipCode();
            this.updatedAt = account.getUpdatedAt();
            this.createdAt = account.getCreatedAt();
        }
    }

}
