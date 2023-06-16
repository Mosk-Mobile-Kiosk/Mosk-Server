package team.mosk.api.server.domain.options.option.dto;

import lombok.*;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateOptionRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotNull(message = "가격은 필수입니다.")
    private Long price;

//    @NotNull(message = "그룹 정보는 필수입니다.")
    private Long optionGroupId;

    public Option toEntity() {
        return Option.builder()
                .name(name)
                .price(price)
                .build();
    }

    public Option toEntity(OptionGroup optionGroup) {
        return Option.builder()
                .name(name)
                .price(price)
                .optionGroup(optionGroup)
                .build();
    }
}
