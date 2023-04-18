package team.mosk.api.server.domain.store.model.persist;

import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.payment.model.persist.Payment;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String storeName;

    private String ownerName;

    private String call;

    private String crn; // 사업자등록 번호

    private LocalDate foundedDate; // 창업일

    private String address;

    @OneToMany(mappedBy = "store")
    private List<Category> categories;

    @OneToMany(mappedBy = "store")
    private List<Product> products;

    @OneToMany(mappedBy = "store")
    private List<Payment> payments;

    @OneToOne(mappedBy = "store")
    private QRCode qrCode;

}
