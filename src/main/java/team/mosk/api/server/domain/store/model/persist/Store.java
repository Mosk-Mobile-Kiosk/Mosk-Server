package team.mosk.api.server.domain.store.model.persist;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.payment.model.persist.Payment;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private String brs; // 사업자등록 여부

    private String address;

    @OneToMany(mappedBy = "store")
    private List<Category> categories;

    @OneToMany(mappedBy = "store")
    private List<Product> products;

    @OneToMany(mappedBy = "store")
    private List<Payment> payments;

    @OneToOne(mappedBy = "store")
    private QRCode qrCode;

    @Builder
    public Store(Long id, String email, String password, String storeName, String ownerName, String call, String brs, String address, List<Category> categories, List<Product> products, List<Payment> payments, QRCode qrCode) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.call = call;
        this.address = address;
        this.categories = categories;
        this.products = products;
        this.payments = payments;
        this.qrCode = qrCode;
    }
}
