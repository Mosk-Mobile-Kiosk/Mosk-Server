package team.mosk.api.server.domain.product.model.persist;

import javax.persistence.*;

@Entity
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id")
    private Long id;

    private String name;

    private String path;

    private String contentType;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;
}
