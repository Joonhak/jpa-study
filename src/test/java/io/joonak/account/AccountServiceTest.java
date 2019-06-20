package io.joonak.account;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.entity.Account;
import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.repository.AccountRepository;
import io.joonak.account.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static io.joonak.account.AccountTestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void 회원가입() {
        //given
        final AccountDto.SignUpRequest dto = buildSignUpRequest();
        given(accountRepository.save(any(Account.class))).willReturn(dto.toEntity());

        //when
        final Account account = accountService.create(dto);

        //then
        verify(accountRepository, atLeastOnce()).save(any(Account.class));

        assertEqualProperties(dto, account);
    }

    @Test
    public void 존재하는_계정() {
        //given
        final AccountDto.SignUpRequest dto = buildSignUpRequest();
        given(accountRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(dto.toEntity()));

        //when
        final Account account = accountService.findById(any(Long.class));

        //then
        verify(accountRepository, atLeastOnce()).findById(any(Long.class));

        assertEqualProperties(dto, account);
    }

    @Test(expected = AccountNotFoundException.class)
    public void 존재하지_않는_계정() {
        //given
        given(accountRepository.findById(any(Long.class))).willReturn(Optional.empty());

        //when
        accountService.findById(any(Long.class));
    }

    @Test
    public void 주소_수정() {
        //given
        final AccountDto.SignUpRequest signUpDto = buildSignUpRequest();
        final AccountDto.UpdateAddressRequest updateDto = buildUpdateRequest();
        given(accountRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(signUpDto.toEntity()));

        //when
        final Account account = accountService.updateAddress(any(Long.class), updateDto);

        //then
        assertEqualAddress(updateDto, account);
    }

}
