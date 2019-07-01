package io.joonak.account.model;

import io.joonak.account.domain.Email;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
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
