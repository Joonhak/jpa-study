package io.joonak.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joonak.account.domain.Address;
import io.joonak.delivery.api.DeliveryController;
import io.joonak.delivery.dto.DeliveryDto;
import io.joonak.delivery.exception.DeliveryAlreadyCompletedException;
import io.joonak.delivery.exception.DeliveryNotFoundException;
import io.joonak.delivery.exception.DeliveryStatusEqualsException;
import io.joonak.delivery.service.DeliveryService;
import io.joonak.error.ErrorCode;
import io.joonak.error.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.joonak.delivery.domain.DeliveryStatus.DELIVERING;
import static io.joonak.utils.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class DeliveryControllerTest {

    @InjectMocks
    private DeliveryController deliveryController;

    @Mock
    private DeliveryService deliveryService;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    private Address address = buildAddress();
    private DeliveryDto.CreationRequest dto = buildDeliveryCreationRequest(address);

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(deliveryController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void 배송정보_생성() throws Exception {
        // given
        given(deliveryService.create(any(DeliveryDto.CreationRequest.class)))
                .willReturn(dto.toEntity());

        // when
        var result = requestCreateDelivery();

        // then
        result
                .andExpect(status().isCreated());
        assertEqualAddress(result, dto.getAddress());
    }

    @Test
    public void 존재하는_배송정보() throws Exception {
        // given
        given(deliveryService.findById(any(Long.class)))
                .willReturn(dto.toEntity());

        // when
        var result = requestGetDelivery();

        // then
        result
                .andExpect(status().isOk());
        assertEqualAddress(result, dto.getAddress());
    }

    @Test
    public void 존재하지_않는_배송정보() throws Exception {
        // given
        given(deliveryService.findById(any(Long.class)))
                .willThrow(DeliveryNotFoundException.class);

        // when
        var result = requestGetDelivery();

        // then
        result
                .andExpect(status().isNotFound());
        assertEqualErrorMessage(result, ErrorCode.DELIVERY_NOT_FOUND);
    }

    @Test
    public void 배송상태_업데이트() throws Exception {
        // given
        var delivery = dto.toEntity();
        delivery.addLog(DELIVERING);

        given(deliveryService.updateDelivery(any(Long.class), any(DeliveryDto.UpdateRequest.class)))
                .willReturn(delivery);

        // when
        var result = requestUpdateDelivery();

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logs.[0].log.status", is(DELIVERING.name())));
        assertEqualAddress(result, dto.getAddress());
    }

    @Test
    public void 같은_상태로_업데이트() throws Exception {
        // given
        var delivery = dto.toEntity();
        delivery.addLog(DELIVERING);

        given(deliveryService.updateDelivery(any(Long.class), any(DeliveryDto.UpdateRequest.class)))
                .willThrow(DeliveryStatusEqualsException.class);

        // when
        var result = requestUpdateDelivery();

        // then
        result
                .andExpect(status().isBadRequest());
        assertEqualErrorMessage(result, ErrorCode.DELIVERY_STATUS_EQUALS);
    }

    @Test
    public void 완료된_배송정보_업데이트() throws Exception {
        // given
        var delivery = dto.toEntity();
        delivery.addLog(DELIVERING);

        given(deliveryService.updateDelivery(any(Long.class), any(DeliveryDto.UpdateRequest.class)))
                .willThrow(DeliveryAlreadyCompletedException.class);

        // when
        var result = requestUpdateDelivery();

        // then
        result
                .andExpect(status().isBadRequest());
        assertEqualErrorMessage(result, ErrorCode.DELIVERY_ALREADY_COMPLETED);
    }

    private ResultActions requestCreateDelivery() throws Exception {
        return mvc.perform(post("/deliveries")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private ResultActions requestGetDelivery() throws Exception {
        return mvc.perform(get("/deliveries/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }

    private ResultActions requestUpdateDelivery() throws Exception {
        return mvc.perform(post("/deliveries/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .characterEncoding("UTF-8")
                .content(mapper.writeValueAsString(DeliveryDto.UpdateRequest.builder().status(DELIVERING).build())))
                .andDo(print());
    }
}
