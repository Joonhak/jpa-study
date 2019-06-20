package io.joonak.account.web;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public AccountDto.Response signUp(@RequestBody final AccountDto.SignUpRequest dto) {
        return new AccountDto.Response(accountService.create(dto));
    }

    @GetMapping("/{id}")
    public AccountDto.Response getAccount(@PathVariable final Long id) {
        return new AccountDto.Response(accountService.findById(id));
    }

    @PatchMapping("/{id}")
    public AccountDto.Response UpdateAddress(@PathVariable final Long id, @RequestBody AccountDto.UpdateAddressRequest dto) {
        return new AccountDto.Response(accountService.updateAddress(id, dto));
    }

}
