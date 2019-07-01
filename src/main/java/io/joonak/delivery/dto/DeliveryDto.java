package io.joonak.delivery.dto;

import io.joonak.account.domain.Address;
import io.joonak.common.domain.DateInfo;
import io.joonak.delivery.domain.Delivery;
import io.joonak.delivery.domain.DeliveryLog;
import io.joonak.delivery.domain.DeliveryStatus;
import lombok.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DeliveryDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreationRequest {
        @Valid
        private Address address;

        @Builder
        public CreationRequest(Address address) {
            this.address = address;
        }

        public Delivery toEntity() {
            return Delivery.builder()
                    .address(address)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRequest {
        private DeliveryStatus status;

        @Builder
        public UpdateRequest(DeliveryStatus status) {
            this.status = status;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Address address;
        private List<LogResponse> logs;

        @Builder
        public Response(Delivery delivery) {
            this.address = delivery.getAddress();
            this.logs = delivery.getLogs()
                    .parallelStream()
                    .map(LogResponse::new)
                    .collect(toList());
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LogResponse {
        private DeliveryLog log;
        private DateInfo dateInfo;

        LogResponse(DeliveryLog log) {
            this.log = log;
            this.dateInfo = log.getDateInfo();
        }
    }

}
