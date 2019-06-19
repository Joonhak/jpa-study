package io.joonak.account.dto;

import io.joonak.account.entity.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequest {
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private String address;
        private String detailAddress;
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
