package team.mosk.api.server.domain.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateProductRequest {

    private Long productId;
    private String name;

    private String description;

    private Long price;
}
