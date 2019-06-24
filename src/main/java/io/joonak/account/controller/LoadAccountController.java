package io.joonak.account.controller;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.service.LoadAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class LoadAccountController {

    private final LoadAccountService loadAccountService;

    @GetMapping("/{id}")
    public AccountDto.Response getAccount(@PathVariable final Long id) {
        return new AccountDto.Response(loadAccountService.findById(id));
    }

}
