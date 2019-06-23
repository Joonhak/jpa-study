package io.joonak.account.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleId {

    ROLE_ADMIN_ID(1L, "ADMIN"),
    ROLE_BASIC_ID(2L, "BASIC");

    private Long id;
    private String name;

}
