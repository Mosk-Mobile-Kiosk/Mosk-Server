package team.mosk.api.server.domain.product.dto;

import lombok.*;
import team.mosk.api.server.domain.product.model.persist.ProductImg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductImgResponse {

    private byte[] file;

    private String contentType;

    public static ProductImgResponse of(final ProductImg productImg) throws IOException {
        byte[] file = Files.readAllBytes(new File(productImg.getPath()).toPath());
        return new ProductImgResponse(file, productImg.getContentType());
    }
}
