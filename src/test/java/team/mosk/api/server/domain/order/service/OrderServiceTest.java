package team.mosk.api.server.domain.order.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;
import team.mosk.api.server.domain.order.dto.CreateOrderRequest;
import team.mosk.api.server.domain.order.dto.OrderResponse;
import team.mosk.api.server.domain.order.model.Order;
import team.mosk.api.server.domain.order.model.OrderRepository;
import team.mosk.api.server.domain.order.vo.OrderStatus;
import team.mosk.api.server.domain.orderproductoption.model.OrderProductOption;
import team.mosk.api.server.domain.orderproductoption.model.OrderProductOptionRepository;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.product.util.GivenProduct;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static team.mosk.api.server.domain.order.vo.OrderStatus.*;

@ActiveProfiles("mac")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private OrderProductOptionRepository orderProductOptionRepository;


    @DisplayName("ProductId와 optionId 리스트를 받아 주문을할 수 있다.")
    @Test
    void createOrder() {
        //given
        Product product1 = productRepository.save(GivenProduct.toEntity());
        Product product2 = productRepository.save(GivenProduct.toEntity());

        Option option1 = Option.builder()
                .name("샷추가")
                .price(1000)
                .build();

        Option option2 = Option.builder()
                .name("얼음 많이")
                .price(500)
                .build();

        Option savedOption1 = optionRepository.save(option1);
        Option savedOption2 = optionRepository.save(option2);

        CreateOrderRequest createOrderRequest1 = new CreateOrderRequest(product1.getId(), List.of(savedOption1.getId(), savedOption2.getId()), 1);
        CreateOrderRequest createOrderRequest2 = new CreateOrderRequest(product2.getId(), List.of(savedOption1.getId()), 2);

        List<CreateOrderRequest> createOrderRequests = List.of(createOrderRequest1, createOrderRequest2);

        LocalDateTime now = LocalDateTime.now();

        //when
        OrderResponse orderResponse = orderService.createOrder(createOrderRequests, now);

        //then
        System.out.println("orderResponse = " + orderResponse);
        Order order = orderRepository.findAll().get(0);
        assertThat(order.getTotalPrice()).isEqualTo(3800);
        assertThat(order.getRegisteredDate()).isEqualTo(now);
        assertThat(order.getOrderStatus()).isEqualTo(INIT);
    }

}