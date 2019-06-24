package io.joonak.account.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Password(String value) {
        this.value = encode(value);
        this.timeToLive = 2_592_000L; // 30 days
        this.expirationDate = setExpirationDate();
    }

    public boolean isExpiration() {
        return LocalDateTime.now().isAfter(this.expirationDate);
    }

    private String encode(String value) {
        return value;
    }

    private LocalDateTime setExpirationDate() {
        return LocalDateTime.now().plusSeconds(timeToLive);
    }

}
