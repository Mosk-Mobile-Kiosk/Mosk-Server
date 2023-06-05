package team.mosk.api.server.domain.order.model.orderproduct;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.order.model.order.Order;
import team.mosk.api.server.domain.order.model.orderproductoption.OrderProductOption;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static OrderProduct of(Order order, Product product, Integer quantity, List<Option> options) {
        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .build();

        if (!options.isEmpty()) {
            List<OrderProductOption> opos = options.stream()
                    .map(option -> OrderProductOption.of(orderProduct, option))
                    .collect(Collectors.toList());

            orderProduct.setOrderProductOptions(opos);
        }

        return orderProduct;
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
