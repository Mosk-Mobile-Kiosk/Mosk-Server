package team.mosk.api.server.domain.subscribe.model;

import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Subscribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long price;
}
