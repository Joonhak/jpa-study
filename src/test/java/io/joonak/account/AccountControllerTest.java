package io.joonak.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joonak.account.api.AccountController;
import io.joonak.account.domain.Account;
import io.joonak.account.domain.Address;
import io.joonak.account.domain.Email;
import io.joonak.account.dto.AccountDto;
import io.joonak.account.exception.EmailDuplicationException;
import io.joonak.account.service.AccountService;
import io.joonak.error.ErrorCode;
import io.joonak.error.ErrorHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.joonak.utils.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private AccountDto.SignUpRequest signUpDto = buildSignUpRequest(buildEmail());

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void 유효한_이메일의_회원가입() throws Exception {
        //given
        given(accountService.create(any(AccountDto.SignUpRequest.class))).willReturn(signUpDto.toEntity());

        //when
        var result = requestSignUp(signUpDto);

        //then
        result
                .andExpect(status().isCreated());
        assertEqualAccount(result, signUpDto.toEntity());
    }

    @Test
    public void 유효하지_않은_이메일의_회원가입() throws Exception {
        //given
        var dto = buildSignUpRequest(Email.builder().address("invalid.com").build());

        //when
        var result = requestSignUp(dto);

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0].field", is("email.address")))
                .andExpect(jsonPath("$.errors.[0].value", is(dto.getEmail().getAddress())));
        assertEqualErrorMessage(result, ErrorCode.INPUT_VALUE_INVALID);
    }

    @Test
    public void 중복된_이메일의_회원가입() throws Exception {
        //given
        given(accountService.create(any())).willThrow(EmailDuplicationException.class);

        //when
        var result = requestSignUp(signUpDto);

        //then
        result
                .andExpect(status().isBadRequest());
        assertEqualErrorMessage(result, ErrorCode.EMAIL_DUPLICATION);
    }

    @Test
    public void 데이터_무결성_위반() throws Exception {
        //given
        given(accountService.create(any())).willThrow(DataIntegrityViolationException.class);

        //when
        var result = requestSignUp(signUpDto);

        //then
        result
                .andExpect(status().isBadRequest());
        assertEqualErrorMessage(result, ErrorCode.KEY_DUPLICATION);
    }

    @Test
    public void 계정정보_아이디() throws Exception {
        //given
        given(accountService.findById(any(Long.class))).willReturn(signUpDto.toEntity());

        //when
        var result = requestGetAccount();

        //then
        result
                .andExpect(status().isOk());
        assertEqualAccount(result, signUpDto.toEntity());
    }

    @Test
    public void 계정정보_이메일() throws Exception {
        //given
        given(accountService.findByEmail(any(Email.class))).willReturn(signUpDto.toEntity());

        //when
        var result = requestGetAccountByEmail(signUpDto.getEmail().getAddress());

        //then
        result
                .andExpect(status().isOk());
        assertEqualAccount(result, signUpDto.toEntity());
    }

    @Test
    public void 계정정보_유효하지_않은_이메일() throws Exception {
        //given

        //when
        var result = requestGetAccountByEmail("invalid.com");

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0].field", is("address")))
                .andExpect(jsonPath("$.errors.[0].value", is("invalid.com")));
        assertEqualErrorMessage(result, ErrorCode.INPUT_VALUE_INVALID);
    }

    @Test
    public void 주소_수정() throws Exception {
        //given
        var dto = buildAccountUpdateRequest();
        var account = Account.builder()
                .email(buildEmail())
                .address(dto.getAddress())
                .build();
        given(accountService.updateAddress(any(Long.class), any(AccountDto.UpdateAddressRequest.class))).willReturn(account);

        //when
        var result = requestAddressUpdate(dto);

        //then
        result
                .andExpect(status().isOk());
        assertEqualAddress(result, dto.getAddress());
    }

    @Test
    public void 주소_수정_실패() throws Exception {
        //given
        var dto = AccountDto.UpdateAddressRequest.builder()
                .address(Address.builder()
                        .address("no-zip-code")
                        .detailAddres("no-zip-code")
                        .build())
                .build();

        //when
        var result = requestAddressUpdate(dto);

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0].field", is("address.zipCode")))
                .andExpect(jsonPath("$.errors.[0].value", is(nullValue())));
        assertEqualErrorMessage(result, ErrorCode.INPUT_VALUE_INVALID);
    }

    private ResultActions requestSignUp(AccountDto.SignUpRequest dto) throws Exception {
        return mvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private ResultActions requestGetAccount() throws Exception {
        return mvc.perform(get("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestGetAccountByEmail(String email) throws Exception {
        return mvc.perform(get("/accounts?email=" + email)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestAddressUpdate(AccountDto.UpdateAddressRequest dto) throws Exception {
        return mvc.perform(patch("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andDo(print());
    }

}
