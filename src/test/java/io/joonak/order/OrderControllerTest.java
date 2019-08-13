package io.joonak.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joonak.error.ErrorCode;
import io.joonak.error.ErrorHandler;
import io.joonak.order.api.OrderController;
import io.joonak.order.domain.Order;
import io.joonak.order.exception.OrderNotFoundException;
import io.joonak.order.service.OrderService;
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

import static io.joonak.utils.TestUtils.assertEqualErrorMessage;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    private Order order = Order.builder().price(10_000D).build();

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void 주문_조회() throws Exception {
        // given
        given(orderService.findById(anyLong()))
                .willReturn(order);

        // when
        var result = requestGetOrder();

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(order.getPrice())));
    }

    @Test
    public void 존재하지_않는_주문_조회() throws Exception {
        // given
        given(orderService.findById(anyLong()))
                .willThrow(OrderNotFoundException.class);

        // when
        var result = requestGetOrder();

        // then
        result.andExpect(status().isNotFound());
        assertEqualErrorMessage(result, ErrorCode.ORDER_NOT_FOUND);
    }

    @Test
    public void 주문_생성() throws Exception {
        // given
        given(orderService.order())
                .willReturn(order);

        // when
        var result = requestCreateOrder();

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.price", is(order.getPrice())));
    }

    private ResultActions requestGetOrder() throws Exception {
        return mvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(order)))
                .andDo(print());
    }

    private ResultActions requestCreateOrder() throws Exception {
        return mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(order)))
                .andDo(print());
    }

}
