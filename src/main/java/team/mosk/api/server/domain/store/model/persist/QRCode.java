package team.mosk.api.server.domain.store.model.persist;

import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;

@Entity
public class QRCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qrcode_id")
    private Long id;

    private String name;

    private String path;

    private String contentType;

    @OneToOne(fetch = FetchType.LAZY)
    private Store store;
}
