package team.mosk.api.server.domain.orderproductoption.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.orderproduct.model.OrderProduct;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @Builder
    public OrderProductOption(Long id, OrderProduct orderProduct, Option option) {
        this.id = id;
        this.orderProduct = orderProduct;
        this.option = option;
    }



    public static OrderProductOption of(OrderProduct orderproduct, Option option) {
        return OrderProductOption.builder()
                .orderProduct(orderproduct)
                .option(option)
                .build();
    }
}
