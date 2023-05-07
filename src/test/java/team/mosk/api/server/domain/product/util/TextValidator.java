package team.mosk.api.server.domain.product.util;

import team.mosk.api.server.domain.product.dto.ProductResponse;

public class TextValidator {

    public static boolean hasText(final String productName, final String keyword) {
        return productName.contains(keyword);
    }
}
