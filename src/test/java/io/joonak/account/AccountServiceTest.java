package io.joonak.account;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.domain.Account;
import io.joonak.account.domain.Email;
import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.exception.EmailDuplicationException;
import io.joonak.account.repository.AccountRepository;
import io.joonak.account.service.LoadAccountService;
import io.joonak.account.service.SaveAccountService;
import io.joonak.account.service.UpdateAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static io.joonak.account.AccountTestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    private SaveAccountService saveAccountService;
    @InjectMocks
    private UpdateAccountService updateAccountService;
    @InjectMocks
    private LoadAccountService loadAccountService;

    @Mock
    private AccountRepository accountRepository;

    private AccountDto.SignUpRequest signUpDto = buildSignUpRequest(Email.builder().address("sign_up@dto.com").build());

    @Test
    public void 회원가입() {
        //given
        given(accountRepository.save(any(Account.class))).willReturn(signUpDto.toEntity());

        //when
        var account = saveAccountService.create(signUpDto);

        //then
        verify(accountRepository, atLeastOnce()).save(any(Account.class));

        assertEqualProperties(signUpDto, account);
    }

    @Test(expected = EmailDuplicationException.class)
    public void 중복된_이메일의_회원가입() {
        //given
        given(accountRepository.existsByEmail(any(Email.class))).willReturn(true);

        //when
        var account = saveAccountService.create(signUpDto);

        //then
        assertThat(account, is(Optional.empty()));
    }

    @Test
    public void 존재하는_계정() {
        //given
        given(accountRepository.findById(any(Long.class))).willReturn(Optional.of(signUpDto.toEntity()));

        //when
        var account = loadAccountService.findById(any(Long.class));

        //then
        verify(accountRepository, atLeastOnce()).findById(any(Long.class));

        assertEqualProperties(signUpDto, account);
    }

    @Test(expected = AccountNotFoundException.class)
    public void 존재하지_않는_계정() {
        //given
        given(accountRepository.findById(any(Long.class))).willReturn(Optional.empty());

        //when
        var account = loadAccountService.findById(any(Long.class));

        //then
        assertThat(account, is(Optional.empty()));
    }

    @Test
    public void 주소_수정() {
        //given
        var updateDto = buildUpdateRequest();
        given(accountRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(signUpDto.toEntity()));

        //when
        var account = updateAccountService.updateAddress(any(Long.class), updateDto);

        //then
        assertEqualAddress(updateDto, account);
    }

    @Test
    public void 존재하는_이메일() {
        //given
        given(accountRepository.existsByEmail(any())).willReturn(true);

        //when
        var isExist = saveAccountService.isExistedEmail(any());

        //then
        assertThat(isExist, is(true));
    }

}
