package io.joonak.account.entity;

import io.joonak.account.dto.AccountDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name= "account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Email email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Account(Email email, String firstName, String lastName, String password, String address, String detailAddress, String zipCode) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
    }

    public void updateAddress(AccountDto.UpdateAddressRequest dto) {
        this.address = dto.getAddress();
        this.detailAddress = dto.getDetailAddress();
        this.zipCode = dto.getZipCode();
    }

}
