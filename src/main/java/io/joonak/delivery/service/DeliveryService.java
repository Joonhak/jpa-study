package io.joonak.delivery.service;

import io.joonak.delivery.domain.Delivery;
import io.joonak.delivery.domain.DeliveryStatus;
import io.joonak.delivery.dto.DeliveryDto;
import io.joonak.delivery.exception.DeliveryNotFoundException;
import io.joonak.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Delivery findById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    public Delivery create(DeliveryDto.CreationRequest dto) {
        var delivery = dto.toEntity();
        delivery.addLog(DeliveryStatus.PENDING);
        return deliveryRepository.save(delivery);
    }

    public Delivery updateDelivery(Long id, DeliveryDto.UpdateRequest dto) {
        var delivery = findById(id);
        delivery.addLog(dto.getStatus());
        return delivery;
    }

}
