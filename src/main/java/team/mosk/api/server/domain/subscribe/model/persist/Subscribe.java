package team.mosk.api.server.domain.subscribe.model.persist;

import lombok.*;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Subscribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private Long id;

    @OneToOne(mappedBy = "subscribe", cascade = CascadeType.ALL)
    private Store store;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long price;
}
