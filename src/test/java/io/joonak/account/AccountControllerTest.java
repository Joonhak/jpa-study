package io.joonak.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joonak.account.controller.AccountController;
import io.joonak.account.dto.AccountDto;
import io.joonak.account.entity.Account;
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

import static io.joonak.account.AccountTestUtils.*;
import static org.hamcrest.Matchers.is;
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

    private AccountDto.SignUpRequest signUpDto = buildSignUpRequest("sign_up@dto.com");

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
        assertEqualProperties(result, signUpDto.toEntity());
    }

    @Test
    public void 유효하지_않은_이메일의_회원가입() throws Exception {
        //given
        var dto = buildSignUpRequest("invalid.com");

        //when
        var result = requestSignUp(dto);

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCode.INPUT_VALUE_INVALID.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCode.INPUT_VALUE_INVALID.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCode.INPUT_VALUE_INVALID.getStatus())))
                .andExpect(jsonPath("$.errors[0].field", is("email")))
                .andExpect(jsonPath("$.errors[0].value", is(dto.getEmail())));
    }

    @Test
    public void 중복된_이메일의_회원가입() throws Exception {
        //given
        given(accountService.create(any())).willThrow(EmailDuplicationException.class);

        //when
        var result = requestSignUp(signUpDto);

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCode.EMAIL_DUPLICATION.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCode.EMAIL_DUPLICATION.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCode.EMAIL_DUPLICATION.getStatus())));
    }

    @Test
    public void 데이터_무결성_위반() throws Exception {
        //given
        given(accountService.create(any())).willThrow(DataIntegrityViolationException.class);

        //when
        var result = requestSignUp(signUpDto);

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCode.KEY_DUPLICATION.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCode.KEY_DUPLICATION.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCode.KEY_DUPLICATION.getStatus())));
    }

    @Test
    public void 계정정보() throws Exception {
        //given
        given(accountService.findById(any(Long.class))).willReturn(signUpDto.toEntity());

        //when
        var result = requestGetAccount();

        //then
        result
                .andExpect(status().isOk());
        assertEqualProperties(result, signUpDto.toEntity());
    }

    @Test
    public void 주소_수정() throws Exception {
        //given
        var dto = buildUpdateRequest();
        var account = Account.builder()
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .zipCode(dto.getZipCode())
                .build();
        given(accountService.updateAddress(any(Long.class), any(AccountDto.UpdateAddressRequest.class))).willReturn(account);

        //when
        var result = requestAddressUpdate(dto);

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is(dto.getAddress())))
                .andExpect(jsonPath("$.detailAddress", is(dto.getDetailAddress())))
                .andExpect(jsonPath("$.zipCode", is(dto.getZipCode())));
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

    private ResultActions requestAddressUpdate(AccountDto.UpdateAddressRequest dto) throws Exception {
        return mvc.perform(patch("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andDo(print());
    }

}
