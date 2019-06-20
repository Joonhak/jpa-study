package io.joonak.account.dto;

import io.joonak.account.entity.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class AccountDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequest {

        @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        private String email;
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
        public SignUpRequest(String email, String firstName, String lastName, String password, String address, String detailAddress, String zipCode) {
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
                    .password(this.password)
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

        public Response(Account account) {
            this.email = account.getEmail();
            this.firstName = account.getFirstName();
            this.lastName = account.getLastName();
            this.address = account.getAddress();
            this.detailAddress = account.getDetailAddress();
            this.zipCode = account.getZipCode();
        }
    }

}
