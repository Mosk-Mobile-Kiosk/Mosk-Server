package team.mosk.api.server.domain.category.dto;

import lombok.*;
import team.mosk.api.server.domain.category.model.persist.Category;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    public static CategoryResponse of(final Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
