package io.joonak.account.controller;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.service.SaveAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class SaveAccountController {

    private final SaveAccountService saveAccountService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public AccountDto.Response signUp(@RequestBody @Valid final AccountDto.SignUpRequest dto) {
        return new AccountDto.Response(saveAccountService.create(dto));
    }

}
