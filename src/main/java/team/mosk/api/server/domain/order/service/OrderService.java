package team.mosk.api.server.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;
import team.mosk.api.server.domain.order.dto.CreateOrderRequest;
import team.mosk.api.server.domain.order.dto.OrderResponse;
import team.mosk.api.server.domain.order.model.Order;
import team.mosk.api.server.domain.order.model.OrderRepository;
import team.mosk.api.server.domain.orderproduct.model.OrderProduct;
import team.mosk.api.server.domain.orderproduct.model.OrderProductRepository;
import team.mosk.api.server.domain.orderproductoption.model.OrderProductOption;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public OrderResponse createOrder(List<CreateOrderRequest> createOrderRequests, LocalDateTime registeredDate) {
        Order order = Order.createInitOrder(registeredDate);
        
        for(CreateOrderRequest createOrderRequest : createOrderRequests) {
            Product product = productRepository.findById(createOrderRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

            OrderProduct orderProduct = OrderProduct.of(order, product, createOrderRequest.getQuantity());
            List<Option> options = optionRepository.findAllById(createOrderRequest.getOptionIds());

            List<OrderProductOption> orderProductOptions = createListBy(orderProduct, options);
            orderProduct.setOrderProductOptions(orderProductOptions);

            order.setOrderProduct(orderProduct);

            long optionTotalPrice = options.stream()
                    .map(Option::getPrice)
                    .mapToLong(value -> value)
                    .sum();
            long productTotalPrice = (product.getPrice() + optionTotalPrice) * createOrderRequest.getQuantity();
            order.plusTotalPrice(productTotalPrice);
        }

        return OrderResponse.of(orderRepository.save(order));
    }

    public void cancel() {
        // TODO: 2023/05/17  
    }
    
    private List<OrderProductOption> createListBy(OrderProduct orderProduct, List<Option> options) {
        return options.stream()
                .map(option -> OrderProductOption.of(orderProduct, option))
                .collect(Collectors.toList());
    }


}



