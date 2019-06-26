package io.joonak.account;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.entity.Account;
import io.joonak.account.entity.Email;
import io.joonak.account.entity.Role;
import io.joonak.account.exception.AccountNotFoundException;
import io.joonak.account.exception.EmailDuplicationException;
import io.joonak.account.repository.AccountRepository;
import io.joonak.account.repository.RoleRepository;
import io.joonak.account.service.LoadAccountService;
import io.joonak.account.service.SaveAccountService;
import io.joonak.account.service.UpdateAccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static io.joonak.account.AccountTestUtils.*;
import static io.joonak.account.constants.RoleId.ROLE_BASIC_ID;
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
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AccountDto.SignUpRequest signUpDto = buildSignUpRequest(new Email("signUp@dto.com"));

    @Test
    public void 회원가입() {
        //given
        given(roleRepository.getOne(ROLE_BASIC_ID.getId())).willReturn(Role.builder().role(ROLE_BASIC_ID).build());
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
        given(accountRepository.findByEmail(any(Email.class))).willReturn(Optional.of(signUpDto.toEntity()));

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
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(signUpDto.toEntity()));

        //when
        var isExist = saveAccountService.isExistedEmail(any());

        //then
        assertThat(isExist, is(true));
    }

}
