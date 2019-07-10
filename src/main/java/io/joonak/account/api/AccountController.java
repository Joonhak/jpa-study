package io.joonak.account.api;

import io.joonak.account.domain.Email;
import io.joonak.account.dto.AccountDto;
import io.joonak.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public AccountDto.Response signUp(@RequestBody @Valid final AccountDto.SignUpRequest dto) {
        return new AccountDto.Response(accountService.create(dto));
    }

    @GetMapping("/{id}")
    public AccountDto.Response getAccount(@PathVariable final Long id) {
        return new AccountDto.Response(accountService.findById(id));
    }

    @GetMapping
    public AccountDto.Response getAccountByEmail(@Valid final Email email) {
        return new AccountDto.Response(accountService.findByEmail(email));
    }

    @PatchMapping("/{id}")
    public AccountDto.Response UpdateAddress(@PathVariable final Long id, @RequestBody @Valid AccountDto.UpdateAddressRequest dto) {
        return new AccountDto.Response(accountService.updateAddress(id, dto));
    }

}
