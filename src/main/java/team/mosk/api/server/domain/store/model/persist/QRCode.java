package team.mosk.api.server.domain.store.model.persist;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QRCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qrcode_id")
    private Long id;

    private String path;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "qrCode")
    private Store store;


    @Builder
    public QRCode(Long id, String path, Store store) {
        this.id = id;
        this.path = path;
        this.store = store;
    }

    public QRCode(String path, Store store) {
        this.path = path;
        this.store = store;
    }
}
