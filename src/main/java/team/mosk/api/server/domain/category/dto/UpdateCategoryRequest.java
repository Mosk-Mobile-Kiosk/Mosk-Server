package team.mosk.api.server.domain.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UpdateCategoryRequest {

    @NotNull(message = "카테고리 아이디는 필수입니다.")
    private Long categoryId;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
}
