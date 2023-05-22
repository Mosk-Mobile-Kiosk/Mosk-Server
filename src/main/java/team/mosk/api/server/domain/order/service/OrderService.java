package team.mosk.api.server.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;
import team.mosk.api.server.domain.order.dto.*;
import team.mosk.api.server.domain.order.error.OrderAccessDeniedException;
import team.mosk.api.server.domain.order.error.OrderNotFoundException;
import team.mosk.api.server.domain.order.model.Order;
import team.mosk.api.server.domain.order.model.OrderRepository;
import team.mosk.api.server.domain.orderproduct.model.OrderProduct;
import team.mosk.api.server.domain.orderproductoption.model.OrderProductOption;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final PaymentService paymentService;

    public OrderResponse createOrder(Long storeId, CreateOrderRequest createOrderRequest, LocalDateTime registeredDate) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        Order order = Order.createInitOrder(store, registeredDate, createOrderRequest.getPaymentKey());
        
        for(OrderProductRequest orderProductRequest : createOrderRequest.getOrderProductRequests()) {
            Product product = productRepository.findById(orderProductRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

            OrderProduct orderProduct = OrderProduct.of(order, product, orderProductRequest.getQuantity());
            List<Option> options = optionRepository.findAllById(orderProductRequest.getOptionIds());

            List<OrderProductOption> orderProductOptions = createListBy(orderProduct, options);
            orderProduct.setOrderProductOptions(orderProductOptions);

            order.setOrderProduct(orderProduct);

            long productTotalPrice = getProductTotalPrice(orderProductRequest.getQuantity(), product, options);
            order.plusTotalPrice(productTotalPrice);
        }

        paymentService.paymentApproval(TossPaymentRequest.of(order, createOrderRequest.getPaymentKey()));

        return OrderResponse.of(orderRepository.save(order));
    }


    public void cancel(Long storeId, Long orderId, String reason) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        if(store != order.getStore()) {
            throw new OrderAccessDeniedException("주문에 접근할 수 없습니다.");
        }

        paymentService.paymentCancel(order.getPaymentKey(), reason);

        order.cancel();
    }

    public void orderCompleted(Long storeId, Long orderId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        if(store != order.getStore()) {
            throw new OrderAccessDeniedException("주문에 접근할 수 없습니다.");
        }

        order.orderCompleted();
    }

    private List<OrderProductOption> createListBy(OrderProduct orderProduct, List<Option> options) {
        return options.stream()
                .map(option -> OrderProductOption.of(orderProduct, option))
                .collect(Collectors.toList());
    }

    private long getProductTotalPrice(int quantity, Product product, List<Option> options) {
        long optionTotalPrice = options.stream()
                .mapToLong(Option::getPrice)
                .sum();
        return (product.getPrice() + optionTotalPrice) * quantity;
    }


}



