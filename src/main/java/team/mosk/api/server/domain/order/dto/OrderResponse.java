package team.mosk.api.server.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import team.mosk.api.server.domain.order.model.order.Order;
import team.mosk.api.server.domain.order.vo.OrderStatus;

import java.time.LocalDateTime;
@ToString
@Getter
@NoArgsConstructor
public class OrderResponse {

    private Long id;

    private OrderStatus orderStatus;

    private int totalPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd 'T' HH:mm")
    private LocalDateTime registeredDate;

    @Builder
    public OrderResponse(Long id, OrderStatus orderStatus, int totalPrice, LocalDateTime registeredDate) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.registeredDate = registeredDate;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .registeredDate(order.getRegisteredDate())
                .build();
    }

}
