package team.mosk.api.server.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team.mosk.api.server.ControllerIntegrationSupport;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;
import team.mosk.api.server.domain.options.option.util.GivenOption;
import team.mosk.api.server.domain.order.dto.CreateOrderRequest;
import team.mosk.api.server.domain.order.dto.OrderProductRequest;

import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.product.util.GivenProduct;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static team.mosk.api.server.domain.order.util.GivenOrder.*;


class OrderControllerTest extends ControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;


    @DisplayName("주문을 할 수 있다.")
    @Test
    void create() throws Exception {
        //given
        Product savedProduct = productRepository.save(GivenProduct.toEntity());
        Option savedOption = optionRepository.save(GivenOption.toEntity());

        OrderProductRequest orderProductRequest = OrderProductRequest.builder()
                .productId(savedProduct.getId())
                .optionIds(List.of(savedOption.getId()))
                .quantity(1)
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .paymentKey(GIVEN_PAYMENT_KEY)
                .orderId(GIVEN_ORDER_ID)
                .orderProductRequests(List.of(orderProductRequest))
                .build();

        String body = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(
                        post("/api/v1/public/{storeId}/orders", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("주문할때 paymentKey는 필수 값이다.")
    @Test
    void createWithoutPaymentKey() throws Exception {
        //given
        Product savedProduct = productRepository.save(GivenProduct.toEntity());
        Option savedOption = optionRepository.save(GivenOption.toEntity());

        OrderProductRequest orderProductRequest = OrderProductRequest.builder()
                .productId(savedProduct.getId())
                .optionIds(List.of(savedOption.getId()))
                .quantity(1)
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .orderId(GIVEN_ORDER_ID)
                .orderProductRequests(List.of(orderProductRequest))
                .build();

        String body = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(
                        post("/api/v1/public/{storeId}/orders", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("결제키는 필수값 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("주문할때 orderId는 필수 값이다.")
    @Test
    void createWithoutOrderId() throws Exception {
        //given
        Product savedProduct = productRepository.save(GivenProduct.toEntity());
        Option savedOption = optionRepository.save(GivenOption.toEntity());

        OrderProductRequest orderProductRequest = OrderProductRequest.builder()
                .productId(savedProduct.getId())
                .optionIds(List.of(savedOption.getId()))
                .quantity(1)
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .paymentKey(GIVEN_PAYMENT_KEY)
                .orderProductRequests(List.of(orderProductRequest))
                .build();

        String body = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(
                        post("/api/v1/public/{storeId}/orders", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("주문번호는 필수값 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("주문할때 상품번호는 필수 값이다.")
    @Test
    void createWithoutOrderProductId() throws Exception {
        //given
        Product savedProduct = productRepository.save(GivenProduct.toEntity());
        Option savedOption = optionRepository.save(GivenOption.toEntity());

        OrderProductRequest orderProductRequest = OrderProductRequest.builder()
                .optionIds(List.of(savedOption.getId()))
                .quantity(1)
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .paymentKey(GIVEN_PAYMENT_KEY)
                .orderId(GIVEN_ORDER_ID)
                .orderProductRequests(List.of(orderProductRequest))
                .build();

        String body = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(
                        post("/api/v1/public/{storeId}/orders", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품번호는 필수값 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("주문할때 상품주문 갯수는 1개 이상이여야 한다.")
    @Test
    void createWithoutProductId() throws Exception {
        //given
        Product savedProduct = productRepository.save(GivenProduct.toEntity());
        Option savedOption = optionRepository.save(GivenOption.toEntity());

        OrderProductRequest orderProductRequest = OrderProductRequest.builder()
                .productId(1l)
                .optionIds(List.of(savedOption.getId()))
                .quantity(0)
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .paymentKey(GIVEN_PAYMENT_KEY)
                .orderId(GIVEN_ORDER_ID)
                .orderProductRequests(List.of(orderProductRequest))
                .build();

        String body = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(
                        post("/api/v1/public/{storeId}/orders", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품주문 갯수는 1개 이상이여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }



}