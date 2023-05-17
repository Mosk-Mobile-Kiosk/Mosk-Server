package team.mosk.api.server.domain.order.model;

import lombok.*;
import team.mosk.api.server.domain.order.vo.OrderStatus;
import team.mosk.api.server.domain.orderproduct.model.OrderProduct;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Order(Long id, OrderStatus orderStatus, int totalPrice, LocalDateTime registeredDate, List<OrderProduct> orderProducts) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.registeredDate = registeredDate;
        this.orderProducts = orderProducts;
    }

    public static Order createInitOrder(LocalDateTime registeredDate) {
        return Order.builder()
                .orderStatus(OrderStatus.INIT)
                .totalPrice(0)
                .registeredDate(registeredDate)
                .orderProducts(new ArrayList<>())
                .build();
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
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
}
