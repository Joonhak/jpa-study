package io.joonak.delivery;

import io.joonak.delivery.domain.Delivery;
import io.joonak.delivery.domain.DeliveryLog;
import io.joonak.delivery.domain.DeliveryStatus;
import io.joonak.delivery.exception.DeliveryAlreadyCompletedException;
import io.joonak.delivery.exception.DeliveryAlreadyDeliveringException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DeliveryLogTest {

    @Test
    public void PENDING_로그_저장() {
        final DeliveryStatus status = DeliveryStatus.PENDING;
        final DeliveryLog log = buildLog(buildDelivery(), status);

        assertThat(status, is(log.getStatus()));
    }

    @Test
    public void DELIVERING_로그_저장() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.PENDING;

        delivery.getLogs().add(buildLog(delivery, status));
        delivery.getLogs().add(buildLog(delivery, DeliveryStatus.DELIVERING));
    }

    @Test
    public void CANCELED_로그_저장() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.PENDING;

        delivery.getLogs().add(buildLog(delivery, status));
        delivery.getLogs().add(buildLog(delivery, DeliveryStatus.CANCELED));
    }

    @Test
    public void COMPLETED_로그_저장() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.DELIVERING;

        delivery.getLogs().add(buildLog(delivery, status));
        delivery.getLogs().add(buildLog(delivery, DeliveryStatus.COMPLETED));
    }

    @Test(expected = DeliveryAlreadyDeliveringException.class)
    public void 배송_시작_후_CANCEL() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.PENDING;

        delivery.getLogs().add(buildLog(delivery, status));
        delivery.getLogs().add(buildLog(delivery, DeliveryStatus.DELIVERING));
    }

    @Test(expected = DeliveryAlreadyCompletedException.class)
    public void 배송_완료_이후_변경() {
        final Delivery delivery = buildDelivery();
        final DeliveryStatus status = DeliveryStatus.COMPLETED;

        delivery.getLogs().add(buildLog(delivery, status));
        delivery.getLogs().add(buildLog(delivery, DeliveryStatus.CANCELED));
    }

    private Delivery buildDelivery() {
        return Delivery.builder().build();
    }

    private DeliveryLog buildLog(Delivery delivery, DeliveryStatus status) {
        return DeliveryLog.builder()
                .delivery(delivery)
                .status(status)
                .build();

    }

}
