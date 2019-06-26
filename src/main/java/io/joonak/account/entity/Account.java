package io.joonak.account.entity;

import io.joonak.account.dto.AccountDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name= "account")
@NamedEntityGraph(name = "AccountWithRoles", attributeNodes = @NamedAttributeNode("roles"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Embedded
    private Password password;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_roles"
            , joinColumns = { @JoinColumn(name = "account_id") }
            , inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Account(Email email, String firstName, String lastName, Password password, String address, String detailAddress, String zipCode) {
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

    public Account setRole(Role role) {
//        role.setAccount(this);
        getRoles().add(role);
        return this;
    }

    public Account setRoles(Set<Role> roles) {
//        roles.forEach(r -> r.setAccount(this));
        getRoles().addAll(roles);
        return this;
    }

}
