package team.mosk.api.server.domain.product.dto;

import lombok.*;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.vo.Selling;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private Long price;

    private Selling selling;

    private List<OptionGroupResponse> optionGroups;

    public ProductResponse(Long id, String name, String description, Long price, Selling selling) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.selling = selling;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSelling(),
                bindingGroup(product.getOptionGroups()));
    }

    public static List<OptionGroupResponse> bindingGroup(List<OptionGroup> optionGroups) {
        return optionGroups.stream()
                .map(optionGroup -> {
                    List<OptionResponse> options = new ArrayList<>();
                    if (optionGroup.getOptions() != null) {
                        options = optionGroup.getOptions().stream()
                                .map(OptionResponse::of)
                                .toList();
                    }
                    return new OptionGroupResponse(
                            optionGroup.getId(),
                            optionGroup.getName(),
                            options
                    );
                })
                .toList();
    }
}
