package io.joonak.account.entity;

import io.joonak.account.constants.RoleId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

//    @ManyToMany(mappedBy = "roles")
//    private Set<Account> accounts = new HashSet<>();

    @Builder
    public Role(RoleId role) {
        this.id = role.getId();
        this.name = role.getName();
    }

//    public Role setAccount(Account account) {
//        getAccounts().add(account);
//        return this;
//    }
//
//    public Role setAccounts(Set<Account> accounts) {
//        getAccounts().addAll(accounts);
//        return this;
//    }

}
