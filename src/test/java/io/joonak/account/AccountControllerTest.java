package io.joonak.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joonak.account.dto.AccountDto;
import io.joonak.account.entity.Account;
import io.joonak.account.service.AccountService;
import io.joonak.account.web.AccountController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void 회원가입() throws Exception {
        //given
        final AccountDto.SignUpRequest dto = buildSignUpRequest();
        given(accountService.create(any(AccountDto.SignUpRequest.class))).willReturn(dto.toEntity());

        //when
        final ResultActions result = requestSignUp(dto);

        //then
        result
                .andExpect(status().isCreated());
        assertEqualProperties(result, dto.toEntity());
    }

    @Test
    public void 계정정보() throws Exception {
        //given
        final AccountDto.SignUpRequest dto = buildSignUpRequest();
        given(accountService.findById(any(Long.class))).willReturn(dto.toEntity());

        //when
        final ResultActions result = requestGetAccount();

        //then
        result
                .andExpect(status().isOk());
        assertEqualProperties(result, dto.toEntity());
    }

    @Test
    public void 주소_수정() throws Exception {
        //given
        final AccountDto.UpdateAddressRequest dto = buildUpdateRequest();
        final Account account = Account.builder()
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .zipCode(dto.getZipCode())
                .build();
        given(accountService.updateAddress(any(Long.class), any(AccountDto.UpdateAddressRequest.class))).willReturn(account);

        //when
        ResultActions result = requestAddressUpdate(dto);

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
