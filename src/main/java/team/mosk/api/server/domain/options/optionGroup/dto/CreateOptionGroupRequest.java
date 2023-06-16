package team.mosk.api.server.domain.options.optionGroup.dto;

import lombok.*;
import team.mosk.api.server.domain.options.option.dto.CreateOptionRequest;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateOptionGroupRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotNull(message = "상품 정보는 필수입니다.")
    private Long productId;

    @NotEmpty(message = "상품 옵션은 필수입니다.")
    private List<CreateOptionRequest> options;

    public OptionGroup toEntity() {
        OptionGroup optionGroup = OptionGroup.builder()
                .name(name)
                .build();

        optionGroup.setOptions(options.stream()
                .map(option -> option.toEntity(optionGroup))
                .toList());

        return optionGroup;
    }


}
