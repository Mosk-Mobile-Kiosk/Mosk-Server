package team.mosk.api.server.domain.product.dto;

import lombok.*;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
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
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private Long price;

    private Selling selling;

    private CategoryResponse category;

    private String imageName;

    private String encodedImg;

    private List<OptionGroupResponse> optionGroups;


    @Builder
    public ProductResponse(Long id, String name, String description, Long price, Selling selling, CategoryResponse category, String imageName, String encodedImg, List<OptionGroupResponse> optionGroups) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.selling = selling;
        this.category = category;
        this.imageName = imageName;
        this.encodedImg = encodedImg;
        this.optionGroups = optionGroups;
    }




    public static ProductResponse of(final Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .selling(product.getSelling())
                .optionGroups(bindingGroup(product.getOptionGroups()))
                .build();
    }

    public static ProductResponse ofWithCategory(final Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .selling(product.getSelling())
                .category(new CategoryResponse(product.getCategory().getId(), product.getCategory().getName()))
                .imageName(product.getProductImg() != null ? product.getProductImg().getName() : null)
                .build();
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
