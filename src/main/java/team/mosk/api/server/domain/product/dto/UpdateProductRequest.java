package team.mosk.api.server.domain.product.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UpdateProductRequest {

    @NotBlank
    private Long productId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private Long price;
}
