package io.joonak.account.dto;

import io.joonak.account.entity.Account;
import io.joonak.account.entity.Email;
import io.joonak.account.entity.Password;
import io.joonak.account.entity.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
                    .password(new Password(this.password))
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
        private Email email;
        private String firstName;
        private String lastName;
        private String address;
        private String detailAddress;
        private String zipCode;
        private LocalDateTime updatedAt;
        private LocalDateTime createdAt;

        public Response(Account account) {
            this.email = account.getEmail();
            this.firstName = account.getFirstName();
            this.lastName = account.getLastName();
            this.address = account.getAddress();
            this.detailAddress = account.getDetailAddress();
            this.zipCode = account.getZipCode();
            this.updatedAt = account.getUpdatedAt();
            this.createdAt = account.getCreatedAt();
        }
    }

    @Getter
    public static class SecurityAccount extends User {
        private Account account;

        private static final String ROLE_PREFIX = "ROLE_";

        public SecurityAccount(Account account) {
            super(account.getEmail().getAddress(), account.getPassword().getValue(), getAuthorities(account.getRoles()));
            this.account = account;
        }

        private static Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
            return roles.stream()
                    .map(r -> new SimpleGrantedAuthority(ROLE_PREFIX + r.getName()))
                    .collect(Collectors.toList());
        }
    }
}
