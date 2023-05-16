package team.mosk.api.server.domain.options.optionGroup.model.persist;

import lombok.*;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.optionGroup.dto.UpdateOptionGroupRequest;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OptionGroup extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_group_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL)
    private List<Option> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * methods
     */

    public void initProduct(final Product product) {
        this.product = product;
    }

    public void update(final UpdateOptionGroupRequest request) {
        this.name = request.getName();
    }
}