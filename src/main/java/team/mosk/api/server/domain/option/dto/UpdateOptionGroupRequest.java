package team.mosk.api.server.domain.option.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateOptionGroupRequest {

    @NotNull(message = "그룹 아이디는 필수입니다.")
    private Long groupId;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;
}
