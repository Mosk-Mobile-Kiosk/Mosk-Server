package team.mosk.api.server.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.options.option.error.OptionNotFoundException;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;
import team.mosk.api.server.domain.order.dto.*;
import team.mosk.api.server.domain.order.error.OrderAccessDeniedException;
import team.mosk.api.server.domain.order.error.OrderNotFoundException;
import team.mosk.api.server.domain.order.error.PaymentGatewayException;
import team.mosk.api.server.domain.order.model.order.Order;
import team.mosk.api.server.domain.order.model.order.OrderRepository;
import team.mosk.api.server.domain.order.model.orderproduct.OrderProduct;
import team.mosk.api.server.domain.order.vo.OrderStatus;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.global.client.PaymentClient;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final PaymentClient paymentClient;


    public OrderResponse createOrder(Long storeId, CreateOrderRequest createOrderRequest, LocalDateTime registeredDate) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND));

        Order order = Order.createInitOrder(store, registeredDate, createOrderRequest.getPaymentKey());
        
        for(OrderProductRequest orderProductRequest : createOrderRequest.getOrderProductRequests()) {
            Product product = productRepository.findById(orderProductRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

            List<Option> options = new ArrayList<>();

            if (!orderProductRequest.getOptionIds().isEmpty()) {
                options = optionRepository.findAllById(orderProductRequest.getOptionIds());
            }

            if (orderProductRequest.getOptionIds().size() != options.size()) {
                throw new OptionNotFoundException(ErrorCode.OPTION_NOT_FOUND);
            }

            OrderProduct orderProduct = OrderProduct.of(order, product, orderProductRequest.getQuantity(), options);

            order.setOrderProduct(orderProduct);

            order.plusTotalPrice(getProductTotalPrice(orderProductRequest.getQuantity(), product, options));
        }

        try {
            paymentClient.paymentApproval(TossPaymentRequest.of(order, createOrderRequest.getPaymentKey()));
        } catch (PaymentGatewayException e) {
            order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(order);
            throw new PaymentGatewayException(ErrorCode.PAYMENT_GATEWAY_UNSTABLE);
        }

        return OrderResponse.of(orderRepository.save(order));
    }

    public void cancel(Long storeId, Long orderId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if(store != order.getStore()) {
            throw new OrderAccessDeniedException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        paymentClient.paymentCancel(order.getPaymentKey());

        order.cancel();
    }

    public void orderCompleted(Long storeId, Long orderId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if(store != order.getStore()) {
            throw new OrderAccessDeniedException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        order.orderCompleted();
    }

    private long getProductTotalPrice(int quantity, Product product, List<Option> options) {
        long optionTotalPrice = 0;

        if (!options.isEmpty()) {
            optionTotalPrice = options.stream()
                    .mapToLong(Option::getPrice)
                    .sum();
        }

        return (product.getPrice() + optionTotalPrice) * quantity;
    }


}



