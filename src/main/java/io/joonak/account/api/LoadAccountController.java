package io.joonak.account.api;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.service.LoadAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
