package team.mosk.api.server.domain.product.model.persist;

import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.product.model.vo.Selling;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    private String description;

    private Long price;

    @Enumerated(EnumType.STRING)
    private Selling selling;

    @OneToOne(mappedBy = "productImg")
    private ProductImg productImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
