package team.mosk.api.server.domain.product.dto;

import lombok.*;
import team.mosk.api.server.domain.product.model.persist.ProductImg;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImgResponse {

    private String encodedImg;
    private String imgType;
    private Long productId;

    public static ProductImgResponse of(final ProductImg productImg, final byte[] bytes) throws Exception {
        String encodedImg = new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
        String imgType = productImg.getName().substring(productImg.getName().indexOf("."));

        return new ProductImgResponse(encodedImg, imgType, productImg.getProduct().getId());
    }
}
