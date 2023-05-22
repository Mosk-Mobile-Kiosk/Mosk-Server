package team.mosk.api.server.domain.category.dto;

import lombok.*;
import team.mosk.api.server.domain.category.model.persist.Category;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateCategoryRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .products(new ArrayList<>())
                .build();
    }

    public static CreateCategoryRequest of(final Category category) {
        return new CreateCategoryRequest(category.getName());
    }
}
