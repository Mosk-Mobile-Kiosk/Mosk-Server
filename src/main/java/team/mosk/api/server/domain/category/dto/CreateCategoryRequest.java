package team.mosk.api.server.domain.category.dto;

import lombok.*;
import team.mosk.api.server.domain.category.model.persist.Category;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateCategoryRequest {

    @NotBlank
    private String name;

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .build();
    }

    public static CreateCategoryRequest of(final Category category) {
        return new CreateCategoryRequest(category.getName());
    }
}
