package team.mosk.api.server.domain.option.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UpdateOptionRequest {

    @NotNull(message = "옵션 아이디는 필수입니다.")
    private Long optionId;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotNull(message = "가격은 필수입니다.")
    private int price;
}
