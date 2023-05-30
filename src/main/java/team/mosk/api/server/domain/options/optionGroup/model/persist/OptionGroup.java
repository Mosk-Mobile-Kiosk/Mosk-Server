package team.mosk.api.server.domain.options.optionGroup.model.persist;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.optionGroup.dto.UpdateOptionGroupRequest;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroup extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_group_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Option> options = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private OptionGroup(Long id, String name, List<Option> options, Product product) {
        this.id = id;
        this.name = name;
        this.options = options;
        this.product = product;
    }

    /**
     * methods
     */

    public void initProduct(final Product product) {
        this.product = product;
    }

    public void update(final UpdateOptionGroupRequest request) {
        this.name = request.getName();
    }

    public void addOption(final Option option) {
        this.options.add(option);
    }

    public void removeOption(final Option option) {
        this.options.remove(option);
    }
}
