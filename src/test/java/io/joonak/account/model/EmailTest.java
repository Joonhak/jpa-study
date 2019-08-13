package io.joonak.account.model;

import io.joonak.account.domain.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
public class EmailTest {

    @Test
    public void 기본_데이터_테스트() {
        var id = "sign_up";
        var host = "dto.com";

        var email = Email.builder()
                .address(id + "@" + host)
                .build();

        assertThat(email.getId(), is(id));
        assertThat(email.getHost(), is(host));
    }

}
