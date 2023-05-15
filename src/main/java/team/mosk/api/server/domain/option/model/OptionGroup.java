package team.mosk.api.server.domain.option.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
}
