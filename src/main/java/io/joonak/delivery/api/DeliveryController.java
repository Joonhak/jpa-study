package io.joonak.delivery.api;

import io.joonak.delivery.dto.DeliveryDto;
import io.joonak.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{id}")
    public DeliveryDto.Response getDelivery(@PathVariable final Long id) {
        return new DeliveryDto.Response(deliveryService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryDto.Response create(@RequestBody @Valid final DeliveryDto.CreationRequest dto) {
        return new DeliveryDto.Response(deliveryService.create(dto));
    }

    @PostMapping("/{id}")
    public DeliveryDto.Response updateDelivery(@PathVariable final Long id, @RequestBody final DeliveryDto.UpdateRequest dto) {
        return new DeliveryDto.Response(deliveryService.updateDelivery(id, dto));
    }

}
