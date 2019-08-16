package io.joonak.delivery;

import io.joonak.delivery.domain.Delivery;
import io.joonak.delivery.domain.DeliveryStatus;
import io.joonak.delivery.exception.DeliveryAlreadyCompletedException;
import io.joonak.delivery.exception.DeliveryAlreadyDeliveringException;
import io.joonak.delivery.exception.DeliveryStatusEqualsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryLogTest {

    @Test
    public void PENDING_로그_저장() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.PENDING;

        delivery.addLog(status);
    }

    @Test
    public void DELIVERING_로그_저장() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.PENDING;

        delivery.addLog(status);
        delivery.addLog(DeliveryStatus.DELIVERING);
    }

    @Test
    public void CANCELED_로그_저장() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.PENDING;

        delivery.addLog(status);
        delivery.addLog(DeliveryStatus.CANCELED);
    }

    @Test
    public void COMPLETED_로그_저장() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.DELIVERING;

        delivery.addLog(status);
        delivery.addLog(DeliveryStatus.COMPLETED);
    }

    @Test
    public void 배송_시작_후_CANCEL() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.DELIVERING;

        delivery.addLog(status);
        assertThrows(DeliveryAlreadyDeliveringException.class, () -> delivery.addLog(DeliveryStatus.CANCELED));
    }

    @Test
    public void 배송_완료_이후_변경() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.COMPLETED;

        delivery.addLog(status);
        assertThrows(DeliveryAlreadyCompletedException.class, () -> delivery.addLog(DeliveryStatus.CANCELED));
    }

    @Test
    public void 같은_상태로_변경() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.PENDING;

        delivery.addLog(status);
        assertThrows(DeliveryStatusEqualsException.class, () -> delivery.addLog(status));
    }

    private Delivery buildDelivery() {
        return Delivery.builder().build();
    }

}
