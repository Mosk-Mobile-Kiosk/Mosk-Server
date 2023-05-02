package team.mosk.api.server.domain.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UpdateCategoryRequest {

    @NotBlank
    private Long categoryId;
    @NotBlank
    private String name;
}
