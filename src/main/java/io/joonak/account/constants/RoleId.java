package io.joonak.account.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleId {

    ROLE_ADMIN_ID(1, "ADMIN"),
    ROLE_BASIC_ID(2, "BASIC");

    private Integer id;
    private String name;

}
