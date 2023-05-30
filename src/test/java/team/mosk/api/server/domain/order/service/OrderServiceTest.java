package team.mosk.api.server.domain.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;
import team.mosk.api.server.domain.order.dto.CreateOrderRequest;
import team.mosk.api.server.domain.order.dto.OrderProductRequest;
import team.mosk.api.server.domain.order.dto.OrderResponse;
import team.mosk.api.server.domain.order.error.OrdeCancelDeniedException;
import team.mosk.api.server.domain.order.error.OrderAccessDeniedException;
import team.mosk.api.server.domain.order.error.OrderCompletedException;
import team.mosk.api.server.domain.order.model.order.Order;
import team.mosk.api.server.domain.order.model.order.OrderRepository;
import team.mosk.api.server.domain.order.vo.OrderStatus;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.product.util.GivenProduct;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static team.mosk.api.server.domain.auth.util.GivenAuth.GIVEN_EMAIL;
import static team.mosk.api.server.domain.auth.util.GivenAuth.GIVEN_PASSWORD;
import static team.mosk.api.server.domain.order.vo.OrderStatus.*;
import static team.mosk.api.server.domain.store.util.GivenStore.*;

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
    private StoreRepository storeRepository;

    @MockBean
    private PaymentService paymentService;


    @DisplayName("ProductId와 optionId 리스트를 받아 주문을할 수 있다.")
    @Test
    void createOrder() {
        //given
        String paymentKey = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();

        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Product product1 = productRepository.save(GivenProduct.toEntity());
        Product product2 = productRepository.save(GivenProduct.toEntity());

        Option option1 = Option.builder()
                .name("샷추가")
                .price(1000l)
                .build();

        Option option2 = Option.builder()
                .name("얼음 많이")
                .price(500l)
                .build();

        Option savedOption1 = optionRepository.save(option1);
        Option savedOption2 = optionRepository.save(option2);

        OrderProductRequest orderProductRequest1 = new OrderProductRequest(product1.getId(), List.of(savedOption1.getId(), savedOption2.getId()), 1);
        OrderProductRequest orderProductRequest2 = new OrderProductRequest(product2.getId(), List.of(savedOption1.getId()), 2);

        List<OrderProductRequest> orderProductRequests = List.of(orderProductRequest1, orderProductRequest2);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .orderProductRequests(orderProductRequests)
                .build();

        LocalDateTime now = LocalDateTime.now();

        //when
        OrderResponse orderResponse = orderService.createOrder(savedStore.getId(), request, now);

        //then
        System.out.println("orderResponse = " + orderResponse);
        Order order = orderRepository.findAll().get(0);
        assertThat(order.getTotalPrice()).isEqualTo(3800);
        assertThat(order.getRegisteredDate()).isEqualTo(now);
        assertThat(order.getOrderStatus()).isEqualTo(INIT);
    }

    @DisplayName("ProductId와 옵션없이 주문을할 수 있다.")
    @Test
    void createOrderWithoutOption() {
        //given
        String paymentKey = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();

        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Product product1 = productRepository.save(GivenProduct.toEntity());
        Product product2 = productRepository.save(GivenProduct.toEntity());

        OrderProductRequest orderProductRequest1 = new OrderProductRequest(product1.getId(), new ArrayList<>(), 1);
        OrderProductRequest orderProductRequest2 = new OrderProductRequest(product2.getId(), new ArrayList<>(), 2);

        List<OrderProductRequest> orderProductRequests = List.of(orderProductRequest1, orderProductRequest2);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .orderProductRequests(orderProductRequests)
                .build();

        LocalDateTime now = LocalDateTime.now();

        //when
        OrderResponse orderResponse = orderService.createOrder(savedStore.getId(), request, now);

        //then
        System.out.println("orderResponse = " + orderResponse);
        Order order = orderRepository.findAll().get(0);
        assertThat(order.getTotalPrice()).isEqualTo(300);
        assertThat(order.getRegisteredDate()).isEqualTo(now);
        assertThat(order.getOrderStatus()).isEqualTo(INIT);
    }

    @DisplayName("주문 상태가 INIT일 경우 주문을 취소할 수 있다.")
    @Test
    void cancelWithOrderStatusINIT() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(store, INIT);
        Order savedOrder = orderRepository.save(order);

        //when
        orderService.cancel(savedStore.getId(), savedOrder.getId());

        //then
        Order findOrder = orderRepository.findById(savedOrder.getId()).get();
        assertThat(findOrder.getOrderStatus()).isEqualTo(CANCELED);
    }

    @DisplayName("주문 상태가 PAYMENT_COMPLETED일 경우 주문을 취소할 수 있다.")
    @Test
    void cancelWithOrderStatusPAYMENT_COMPLETED() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(store, PAYMENT_COMPLETED);
        Order savedOrder = orderRepository.save(order);

        //when
        orderService.cancel(savedStore.getId(), savedOrder.getId());

        //then
        Order findOrder = orderRepository.findById(savedOrder.getId()).get();
        assertThat(findOrder.getOrderStatus()).isEqualTo(CANCELED);
    }



    @DisplayName("자신의 가게의 주문이 아닐경우 주문을 취소할 수 없다.")
    @Test
    void cancelWithOtherStore() {
        //given
        Store store1 = createStore("test1@test1");
        Store store2 = createStore("test2@test2");

        Store savedStore1 = storeRepository.save(store1);
        Store savedStore2 = storeRepository.save(store2);

        Order order = createStore(store1, INIT);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.cancel(savedStore2.getId(), savedOrder.getId()))
                .isInstanceOf(OrderAccessDeniedException.class)
                .hasMessage("주문에 접근할 수 없습니다.");
    }

    @DisplayName("주문 상태가 CANCELED일 경우 주문을 취소할 수 없다.")
    @Test
    void cancelWithOrderStatusCANCELED() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(savedStore, CANCELED);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.cancel(savedStore.getId(), savedOrder.getId()))
                .isInstanceOf(OrdeCancelDeniedException.class)
                .hasMessage("주문상태:CANCELED는 주문 취소가 불가능합니다.");
    }

    @DisplayName("주문 상태가 PAYMENT_FAILED 경우 주문을 취소할 수 없다.")
    @Test
    void cancelWithOrderStatusPAYMENT_FAILED() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(savedStore, PAYMENT_FAILED);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.cancel(savedStore.getId(), savedOrder.getId()))
                .isInstanceOf(OrdeCancelDeniedException.class)
                .hasMessage("주문상태:PAYMENT_FAILED는 주문 취소가 불가능합니다.");
    }

    @DisplayName("주문 상태가 COMPLETED 경우 주문을 취소할 수 없다.")
    @Test
    void cancelWithOrderStatusCOMPLETED() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(savedStore, COMPLETED);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.cancel(savedStore.getId(), savedOrder.getId()))
                .isInstanceOf(OrdeCancelDeniedException.class)
                .hasMessage("주문상태:COMPLETED는 주문 취소가 불가능합니다.");
    }

    @DisplayName("주문처리 완료를 하기 위해서는 주문 상태가 PAYMENT_COMPLETED 상태여야 한다.")
    @Test
    void orderCompleted() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(savedStore, PAYMENT_COMPLETED);
        Order savedOrder = orderRepository.save(order);

        //when
        orderService.orderCompleted(savedStore.getId(), savedOrder.getId());

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(COMPLETED);
    }

    @DisplayName("자신의 가게의 주문이 아닐경우 주문을 완료할 수 없다.")
    @Test
    void orderCompletedWithOtherStore() {
        //given
        Store store1 = createStore("test1@test1");
        Store store2 = createStore("test2@test2");

        Store savedStore1 = storeRepository.save(store1);
        Store savedStore2 = storeRepository.save(store2);

        Order order = createStore(savedStore1, PAYMENT_COMPLETED);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.orderCompleted(savedStore2.getId(), savedOrder.getId()))
                .isInstanceOf(OrderAccessDeniedException.class)
                .hasMessage("주문에 접근할 수 없습니다.");
    }

    @DisplayName("주문상태가 INIT일 경우 주문을 완료할 수 없다.")
    @Test
    void orderCompletedWithOrderStatusINIT() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(savedStore, INIT);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.orderCompleted(savedStore.getId(), savedOrder.getId()))
                .isInstanceOf(OrderCompletedException.class)
                .hasMessage("주문 처리를 완료하기 위해서는 주문이 결제완료 상태여야 합니다.");
    }

    @DisplayName("주문상태가 CANCELED일 경우 주문을 완료할 수 없다.")
    @Test
    void orderCompletedWithOrderStatusCANCELED() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(savedStore, CANCELED);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.orderCompleted(savedStore.getId(), savedOrder.getId()))
                .isInstanceOf(OrderCompletedException.class)
                .hasMessage("주문 처리를 완료하기 위해서는 주문이 결제완료 상태여야 합니다.");
    }

    @DisplayName("주문상태가 PAYMENT_FAILED일 경우 주문을 완료할 수 없다.")
    @Test
    void orderCompletedWithOrderStatusPAYMENT_FAILED() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(savedStore, PAYMENT_FAILED);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.orderCompleted(savedStore.getId(), savedOrder.getId()))
                .isInstanceOf(OrderCompletedException.class)
                .hasMessage("주문 처리를 완료하기 위해서는 주문이 결제완료 상태여야 합니다.");
    }
    

    @DisplayName("주문상태가 PAYMENT_FAILED일 경우 주문을 완료할 수 없다.")
    @Test
    void orderCompletedWithOrderStatusCOMPLETED() {
        //given
        Store store = createStore(GIVEN_EMAIL);
        Store savedStore = storeRepository.save(store);

        Order order = createStore(savedStore, COMPLETED);
        Order savedOrder = orderRepository.save(order);

        //when //then
        assertThatThrownBy(() -> orderService.orderCompleted(savedStore.getId(), savedOrder.getId()))
                .isInstanceOf(OrderCompletedException.class)
                .hasMessage("주문 처리를 완료하기 위해서는 주문이 결제완료 상태여야 합니다.");
    }


    private Store createStore(String email) {
        Store store = Store.builder()
                .email(email)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();
        return store;
    }

    private static Order createStore(Store store, OrderStatus orderStatus) {
        return Order.builder()
                .orderStatus(orderStatus)
                .store(store)
                .build();
    }

}