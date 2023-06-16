package team.mosk.api.server.domain.store.model.persist;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.subscribe.model.persist.Subscribe;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
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

    @Column(name = "call_number")
    private String call;

    private String crn; // 사업자등록 번호

    private String address;

    @OneToMany(mappedBy = "store")
    private List<Category> categories;

    @OneToMany(mappedBy = "store")
    private List<Product> products;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qrCode_id")
    private QRCode qrCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribe_id")
    private Subscribe subscribe;

    @Builder
    public Store(Long id, String email, String password, String storeName, String ownerName, String call, String crn, String address, List<Category> categories, List<Product> products, QRCode qrCode) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.call = call;
        this.crn = crn;
        this.address = address;
        this.categories = categories;
        this.products = products;
        this.qrCode = qrCode;
    }

    public void setEncodePassword(String encodePassword) {
        this.password = encodePassword;
    }

    public void update(StoreUpdateRequest request) {
        this.storeName = request.getStoreName();
        this.ownerName = request.getOwnerName();
        this.call = request.getCall();
        this.address = request.getAddress();
    }

    public void setQrCode(QRCode qrCode) {
        this.qrCode = qrCode;
    }
}
