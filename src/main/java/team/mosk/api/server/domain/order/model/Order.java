package team.mosk.api.server.domain.order.model;

import lombok.*;
import team.mosk.api.server.domain.order.error.OrdeCancelDeniedException;
import team.mosk.api.server.domain.order.error.OrderCompletedException;
import team.mosk.api.server.domain.order.vo.OrderStatus;
import team.mosk.api.server.domain.orderproduct.model.OrderProduct;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static team.mosk.api.server.domain.order.vo.OrderStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "orderProducts")
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Order(Long id, OrderStatus orderStatus, int totalPrice, LocalDateTime registeredDate, List<OrderProduct> orderProducts, Store store) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.registeredDate = registeredDate;
        this.orderProducts = orderProducts;
        this.store = store;
    }




    public static Order createInitOrder(Store store, LocalDateTime registeredDate) {
        return Order.builder()
                .orderStatus(INIT)
                .totalPrice(0)
                .registeredDate(registeredDate)
                .orderProducts(new ArrayList<>())
                .store(store)
                .build();
    }

    public void plusTotalPrice(long price) {
        this.totalPrice += price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    public void cancel() {
        if(orderStatus != INIT && orderStatus != PAYMENT_COMPLETED) {
            throw new OrdeCancelDeniedException("주문상태:" + this.orderStatus + "는 주문 취소가 불가능합니다.");
        }

        this.orderStatus = CANCELED;
    }

    public void orderCompleted() {
        if (this.orderStatus != PAYMENT_COMPLETED) {
            throw new OrderCompletedException("주문 처리를 완료하기 위해서는 주문이 결제완료 상태여야 합니다.");
        }
        this.orderStatus = COMPLETED;
    }
}
