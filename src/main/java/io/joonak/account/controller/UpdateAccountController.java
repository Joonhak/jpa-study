package io.joonak.account.controller;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.service.UpdateAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class UpdateAccountController {

    private final UpdateAccountService updateAccountService;

    @PatchMapping("/{id}")
    public AccountDto.Response UpdateAddress(@PathVariable final Long id, @RequestBody AccountDto.UpdateAddressRequest dto) {
        return new AccountDto.Response(updateAccountService.updateAddress(id, dto));
    }

}
