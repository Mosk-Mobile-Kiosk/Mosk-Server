package team.mosk.api.server.domain.category.dto;

import lombok.*;
import team.mosk.api.server.domain.category.model.persist.Category;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SimpleCategoryResponse {

    private Long id;

    private String name;

    public static SimpleCategoryResponse of(final Category category) {
        return new SimpleCategoryResponse(category.getId(), category.getName());
    }
}
