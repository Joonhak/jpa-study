package io.joonak.account.model;

import io.joonak.account.domain.Password;
import io.joonak.account.exception.PasswordFailedExceededException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static io.joonak.utils.TestUtils.buildPassword;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class PasswordTest {


    private String passwordString = "test-password";

    @Test
    public void 기본_데이터_테스트() {
        var password = buildPassword(passwordString);
        assertThat(password.isMatched(passwordString), is(true));
        assertThat(password.isExpiration(), is(false));
        assertThat(password.getFailedCount(), is(0));
        assertThat(password.getValue().startsWith("{bcrypt}"), is(true));
        assertThat(password.getExpirationDate().isAfter(LocalDateTime.now()), is(true));
    }

    @Test
    public void 비밀번호가_일치하면_실패횟수_초기화() {
        var password = buildPassword(passwordString);

        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");

        assertThat(password.getFailedCount(), is(3));

        password.isMatched(passwordString);

        assertThat(password.getFailedCount(), is(0));
    }

    @Test
    public void 비밀번호가_일치하지_않으면_실패횟수_증가() {
        var password = buildPassword(passwordString);

        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");

        assertThat(password.getFailedCount(), is(3));
    }

    @Test(expected = PasswordFailedExceededException.class)
    public void 비밀번호_5회_초과_실패시_EXCEPTION() {
        var password = buildPassword(passwordString);

        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
    }

    @Test
    public void 비밀번호_4회_실패_후_일치해도_초기화() {
        var password = buildPassword(passwordString);

        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");

        assertThat(password.isMatched(passwordString), is(true));
        assertThat(password.getFailedCount(), is(0));
    }

    @Test
    public void 비밀번호가_일치해야_변경가능() {
        var password = buildPassword(passwordString);
        var newPasswordString = "new-password";

        password.isMatched("invalid"); // fail count ++

        password.changeValue(passwordString, newPasswordString);

        assertThat(password.isMatched(newPasswordString), is(true));
        assertThat(password.isExpiration(), is(false));
        assertThat(password.getFailedCount(), is(0));
        assertThat(password.getValue().startsWith("{bcrypt}"), is(true));
    }

    @Test(expected = PasswordFailedExceededException.class)
    public void 비밀번호_5회_틀리면_비밀번호_변경불가() {
        var password = buildPassword(passwordString);
        var newPasswordString = "new-password";

        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");

        password.changeValue(passwordString, newPasswordString);
    }

}
