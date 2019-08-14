package io.joonak.account;

import io.joonak.account.dto.AccountSearchType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("local")
public class AccountIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("ACCOUNT_INTEGRATION 이름으로 계정 조회요청")
    public void getAccountsByName() throws Exception {
        var result = pageableRequest(AccountSearchType.NAME, "spring", 20);

        result
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("ACCOUNT_INTEGRATION 이메일로 계정 조회요청")
    public void getAccountsByEmail() throws Exception {
        var result = pageableRequest(AccountSearchType.EMAIL, "test01", 20);

        result
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("ACCOUNT_INTEGRATION 비정상적인 사이즈로 계정 조회요청")
    public void getAccountsByNameIllegalSize() throws Exception {
        var result = pageableRequest(AccountSearchType.NAME, "go", Integer.MAX_VALUE);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size",is(10)));
    }

    private ResultActions pageableRequest(AccountSearchType type, String value, int size) throws Exception {
        return mvc.perform(get("/accounts/all")
                .contentType(MediaType.TEXT_PLAIN)
                .param("page", "1")
                .param("size", "" + size)
                .param("type", type.name())
                .param("value", value))
                .andDo(print());
    }

}
