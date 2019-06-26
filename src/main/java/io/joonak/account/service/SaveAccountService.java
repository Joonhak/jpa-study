package io.joonak.account.service;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.entity.Account;
import io.joonak.account.entity.Email;
import io.joonak.account.entity.Role;
import io.joonak.account.exception.EmailDuplicationException;
import io.joonak.account.repository.AccountRepository;
import io.joonak.account.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static io.joonak.account.constants.RoleId.ROLE_ADMIN_ID;
import static io.joonak.account.constants.RoleId.ROLE_BASIC_ID;

@Service
@Transactional
@RequiredArgsConstructor
public class SaveAccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    // TODO: find better way
    @PostConstruct
    public void setRoles() {
        var roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            var adminRole = Role.builder().role(ROLE_ADMIN_ID).build();
            var basicRole = Role.builder().role(ROLE_BASIC_ID).build();
            roleRepository.saveAll(List.of(adminRole, basicRole));
        }
    }

    public Account create(AccountDto.SignUpRequest dto) {
        if (isExistedEmail(dto.getEmail()))
            throw new EmailDuplicationException(dto.getEmail());
        dto.encodePassword(passwordEncoder);
        var entity = dto.toEntity().setRole(roleRepository.getOne(ROLE_BASIC_ID.getId()));
        return accountRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public boolean isExistedEmail(Email email) {
        return accountRepository.findByEmail(email).isPresent();
    }

}
