package team.mosk.api.server.domain.option.dto;

import lombok.*;
import team.mosk.api.server.domain.option.model.persist.OptionGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    public OptionGroup toEntity() {
        return OptionGroup.builder()
                .name(name)
                .build();
    }
}
