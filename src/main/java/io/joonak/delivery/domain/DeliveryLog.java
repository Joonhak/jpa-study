package io.joonak.delivery.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.joonak.common.domain.DateInfo;
import io.joonak.delivery.exception.DeliveryAlreadyCompletedException;
import io.joonak.delivery.exception.DeliveryAlreadyDeliveringException;
import io.joonak.delivery.exception.DeliveryStatusEqualsException;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @ToString(exclude = "delivery")
@Table(name = "delivery_log")
@JsonIgnoreProperties({"lastStatus"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = false)
    private DeliveryStatus status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false, updatable = false)
    private Delivery delivery;

    @Embedded
    private DateInfo dateInfo;

    @Transient
    private DeliveryStatus lastStatus;

    @Builder
    public DeliveryLog(DeliveryStatus status, Delivery delivery) {
        verifyStatus(status, delivery);

        if (status == DeliveryStatus.CANCELED)
            cancel();
        else
            this.status = status;

        this.delivery = delivery;
    }

    private void verifyStatus(DeliveryStatus status, Delivery delivery) {
        lastStatus = getLastStatus(delivery);
        if (lastStatus != null) {
            verifyWithLastStatus(status, lastStatus);
        }
    }

    private void cancel() {
        if (isPending())
            this.status = DeliveryStatus.CANCELED;
        else
            throw new DeliveryAlreadyDeliveringException();
    }

    private boolean isPending() {
        return this.lastStatus == DeliveryStatus.PENDING;
    }

    private boolean isCompleted() {
        return this.lastStatus == DeliveryStatus.COMPLETED;
    }

    private DeliveryStatus getLastStatus(Delivery delivery) {
        final int lastIndex = delivery.getLogs().size() - 1;
        return lastIndex > -1 ? delivery.getLogs().get(lastIndex).getStatus() : null;
    }

    private void verifyWithLastStatus(DeliveryStatus newStatus, DeliveryStatus lastStatus) {
        if (isCompleted())
            throw new DeliveryAlreadyCompletedException();
        if (newStatus == lastStatus)
            throw new DeliveryStatusEqualsException(newStatus);
    }

}
