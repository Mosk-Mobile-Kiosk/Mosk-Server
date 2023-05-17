package team.mosk.api.server.domain.options.optionGroup.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.product.model.persist.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionGroupResponse {

    private Long id;
    private String name;
    private List<OptionResponse> options;
    private String productName;

    public static OptionGroupResponse of(final OptionGroup optionGroup) {
        if(optionGroup.getOptions() != null) {
            List<OptionResponse> options = optionGroup.getOptions().stream()
                    .map(option -> new OptionResponse(option.getId(), option.getName(), option.getPrice()))
                    .toList();
            return new OptionGroupResponse(optionGroup.getId(), optionGroup.getName(), options, optionGroup.getProduct().getName());
        }

        List<OptionResponse> options = new ArrayList<>();
        return new OptionGroupResponse(optionGroup.getId(), optionGroup.getName(), options, optionGroup.getProduct().getName());
    }
}
