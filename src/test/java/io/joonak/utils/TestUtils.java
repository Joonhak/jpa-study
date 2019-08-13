package io.joonak.utils;

import io.joonak.account.domain.Account;
import io.joonak.account.domain.Address;
import io.joonak.account.domain.Email;
import io.joonak.account.domain.Password;
import io.joonak.account.dto.AccountDto;
import io.joonak.common.domain.PageRequest;
import io.joonak.delivery.domain.DeliveryStatus;
import io.joonak.delivery.dto.DeliveryDto;
import io.joonak.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestUtils {

    @Test
    public void method_template() {
        // given
        // when
        // then
    }

    public static AccountDto.UpdateAddressRequest buildAccountUpdateRequest() {
        return AccountDto.UpdateAddressRequest.builder()
                .address(buildAddress("서울특별시", "송파구", "12345"))
                .build();
    }

    public static AccountDto.SignUpRequest buildSignUpRequest(Email email) {
        return AccountDto.SignUpRequest.builder()
                .email(email)
                .firstName("이름")
                .lastName("성")
                .password("qwerty")
                .address(buildAddress())
                .build();
    }

    public static List<Account> buildAccountList() {
        var address = Address.builder().address("korea").detailAddres("seoul").zipCode("12345").build();
        var password = Password.builder().value("test").build();
        return List.of(
                Account.builder()
                        .email(Email.builder().address("test1@asd.com").build())
                        .address(address)
                        .firstName("first")
                        .lastName("last")
                        .password(password)
                        .build(),
                Account.builder()
                        .email(Email.builder().address("test2@asd.com").build())
                        .address(address)
                        .firstName("first")
                        .lastName("last")
                        .password(password)
                        .build()
        );
    }

    public static Page<Account> buildPageAccount(int size) {
        var pageRequest = new PageRequest();
        pageRequest.setPage(0);
        pageRequest.setSize(size);
        return new PageImpl<>(buildAccountList(), pageRequest.of(), 20);
    }

    public static void assertEqualAccount(ResultActions result, Account account) throws Exception {
        result
                .andExpect(jsonPath("$.email", is(account.getEmail().getAddress())))
                .andExpect(jsonPath("$.firstName", is(account.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(account.getLastName())));
        assertEqualAddress(result, account.getAddress());
    }

    public static void assertEqualAccount(AccountDto.SignUpRequest dto, Account account) {
        assertEquals(dto.getEmail().getAddress(), account.getEmail().getAddress());
        assertEquals(dto.getFirstName(), account.getFirstName());
        assertEquals(dto.getLastName(), account.getLastName());
        assertEquals(dto.getAddress(), account.getAddress());
        assertEquals(dto.getAddress().getDetailAddress(), account.getAddress().getDetailAddress());
        assertEquals(dto.getAddress().getZipCode(), account.getAddress().getZipCode());
    }

    public static void assertEqualAddress(ResultActions result, Address address) throws Exception {
        result
                .andExpect(jsonPath("$.address.address", is(address.getAddress())))
                .andExpect(jsonPath("$.address.detailAddress", is(address.getDetailAddress())))
                .andExpect(jsonPath("$.address.zipCode", is(address.getZipCode())));
    }

    public static void assertEqualAddress(AccountDto.UpdateAddressRequest dto, Account account) {
        assertEquals(dto.getAddress(), account.getAddress());
        assertEquals(dto.getAddress().getDetailAddress(), account.getAddress().getDetailAddress());
        assertEquals(dto.getAddress().getZipCode(), account.getAddress().getZipCode());
    }

    public static void assertEqualErrorMessage(ResultActions result, ErrorCode errorCode) throws Exception {
        result
                .andExpect(jsonPath("$.message", is(errorCode.getMessage())))
                .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                .andExpect(jsonPath("$.status", is(errorCode.getStatus())));
    }

    public static Email buildEmail() {
        return Email.builder()
                .address("sign_up@dto.com")
                .build();
    }

    public static Email buildEmail(String address) {
        return Email.builder()
                .address(address)
                .build();
}

    public static Address buildAddress() {
        return Address.builder()
                .address("경기도")
                .detailAddres("부천시")
                .zipCode("78965")
                .build();
    }

    public static Address buildAddress(String address, String detailAddress, String zipCode) {
        return Address.builder()
                .address(address)
                .detailAddres(detailAddress)
                .zipCode(zipCode)
                .build();
    }

    public static Password buildPassword(String value) {
        return Password.builder()
                .value(value)
                .build();
    }

    public static DeliveryDto.CreationRequest buildDeliveryCreationRequest(Address address) {
        return DeliveryDto.CreationRequest.builder()
                .address(address)
                .build();
    }

    public static DeliveryDto.UpdateRequest buildDeliveryUpdateRequest(DeliveryStatus status) {
        return DeliveryDto.UpdateRequest.builder()
                .status(status)
                .build();
    }

}
