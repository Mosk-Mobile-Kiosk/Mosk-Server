package team.mosk.api.server.domain.options.optionGroup.dto;

import lombok.*;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.product.model.persist.Product;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class OptionGroupResponse {

    private Long id;
    private String name;
    private List<Option> options;
    private Product product;

    public static OptionGroupResponse of(final OptionGroup optionGroup) {
        return new OptionGroupResponse(optionGroup.getId(), optionGroup.getName(), optionGroup.getOptions(), optionGroup.getProduct());
    }
}
