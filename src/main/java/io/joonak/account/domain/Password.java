package io.joonak.account.domain;

import io.joonak.account.exception.PasswordFailedExceededException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    @Column(name = "password", nullable = false)
    private String value;

    @Column(name = "password_expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "password_failed_count", nullable = false)
    private int failedCount;

    @Column(name = "password_time_to_live")
    private Long timeToLive;

    @Builder
    public Password(String value) {
        this.value = encode(value);
        this.timeToLive = 2_592_000L; // 30 days
        this.expirationDate = extendExpirationDate();
    }

    public boolean isMatched(String password) {
        if (failedCount >= 5)
            throw new PasswordFailedExceededException();

        final var matched = isMatches(password);
        updateFailedCount(matched);
        return matched;
    }

    public boolean isExpiration() {
        return LocalDateTime.now().isAfter(this.expirationDate);
    }

    private String encode(String password) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
    }

    private boolean isMatches(String password) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(password, this.value);
    }

    private void updateFailedCount(boolean matches) {
        if (matches)
            resetFailCount();
        else
            increaseFailCount();
    }

    private void resetFailCount() {
        this.failedCount = 0;
    }

    private void increaseFailCount() {
        this.failedCount++;
    }

    private LocalDateTime extendExpirationDate() {
        return LocalDateTime.now().plusSeconds(timeToLive);
    }

}
