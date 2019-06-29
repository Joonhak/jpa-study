package io.joonak.account;

import io.joonak.account.dto.AccountDto;
import io.joonak.account.domain.Account;
import io.joonak.account.domain.Email;
import io.joonak.error.ErrorCode;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AccountTestUtils {

    static AccountDto.UpdateAddressRequest buildUpdateRequest() {
        return AccountDto.UpdateAddressRequest.builder()
                .address("서울특별시")
                .detailAddress("송파구")
                .zipCode("12345")
                .build();
    }

    static AccountDto.SignUpRequest buildSignUpRequest(Email email) {
        return AccountDto.SignUpRequest.builder()
                .email(email)
                .firstName("이름")
                .lastName("성")
                .password("qwerty")
                .address("경기도")
                .detailAddress("부천시")
                .zipCode("78965")
                .build();
    }

    static void assertEqualProperties(ResultActions result, Account account) throws Exception {
        result
                .andExpect(jsonPath("$.email", is(account.getEmail().getAddress())))
                .andExpect(jsonPath("$.firstName", is(account.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(account.getLastName())))
                .andExpect(jsonPath("$.address", is(account.getAddress())))
                .andExpect(jsonPath("$.detailAddress", is(account.getDetailAddress())))
                .andExpect(jsonPath("$.zipCode", is(account.getZipCode())));
    }

    static void assertEqualProperties(AccountDto.SignUpRequest dto, Account account) {
        assertEquals(dto.getEmail().getAddress(), account.getEmail().getAddress());
        assertEquals(dto.getFirstName(), account.getFirstName());
        assertEquals(dto.getLastName(), account.getLastName());
        assertEquals(dto.getAddress(), account.getAddress());
        assertEquals(dto.getDetailAddress(), account.getDetailAddress());
        assertEquals(dto.getZipCode(), account.getZipCode());
    }

    static void assertEqualAddress(AccountDto.UpdateAddressRequest dto, Account account) {
        assertEquals(dto.getAddress(), account.getAddress());
        assertEquals(dto.getDetailAddress(), account.getDetailAddress());
        assertEquals(dto.getZipCode(), account.getZipCode());
    }

    static void assertEqualMessage(ResultActions result, ErrorCode errorCode) throws Exception {
        result
                .andExpect(jsonPath("$.message", is(errorCode.getMessage())))
                .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                .andExpect(jsonPath("$.status", is(errorCode.getStatus())));
    }

}
