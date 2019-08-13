package io.joonak.account.model;

import io.joonak.account.exception.PasswordFailedExceededException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static io.joonak.utils.TestUtils.buildPassword;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
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

    @Test
    public void 비밀번호_5회_초과_실패시_EXCEPTION() {
        var password = buildPassword(passwordString);

        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");

        assertThrows(PasswordFailedExceededException.class, () -> password.isMatched("invalid"));
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

    @Test
    public void 비밀번호_5회_틀리면_비밀번호_변경불가() {
        var password = buildPassword(passwordString);
        var newPasswordString = "new-password";

        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");
        password.isMatched("invalid");

        assertThrows(PasswordFailedExceededException.class, () -> password.changeValue(passwordString, newPasswordString));
    }

}
