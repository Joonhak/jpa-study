package io.joonak.account.entity;

import io.joonak.account.constants.RoleId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts = new HashSet<>();

    @Builder
    public Role(RoleId role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    public Role setAccount(Account account) {
        account.setRole(this);
        getAccounts().add(account);
        return this;
    }

    public Role setAccounts(Set<Account> accounts) {
        accounts.forEach(a -> a.setRole(this));
        getAccounts().addAll(accounts);
        return this;
    }

}
