package team.mosk.api.server.domain.options.option.model.persist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.options.option.dto.UpdateOptionRequest;
import team.mosk.api.server.global.common.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Option extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    private String name;

    private int price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "option_group_id")
    @JsonBackReference
    private OptionGroup optionGroup;

    /**
     * methods
     */

    public void initGroup(final OptionGroup optionGroup) {
        this.optionGroup = optionGroup;
    }

    public void update(final UpdateOptionRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
    }
}
