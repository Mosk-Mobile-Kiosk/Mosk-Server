package team.mosk.api.server.domain.orderproduct.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.order.model.Order;
import team.mosk.api.server.domain.orderproductoption.model.OrderProductOption;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    @OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private List<OrderProductOption> orderProductOptions = new ArrayList<>();

    @Builder
    public OrderProduct(Long id, Integer quantity, Product product, Order order, List<OrderProductOption> orderProductOptions) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.order = order;
        this.orderProductOptions = orderProductOptions;
    }

    public void setOrderProductOptions(List<OrderProductOption> orderProductOptions) {
        this.orderProductOptions = orderProductOptions;
    }

    public static OrderProduct of(Order order, Product product, Integer quantity) {
        return OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderProduct that = (OrderProduct) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
