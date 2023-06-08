package team.mosk.api.server.domain.product.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class ProductNotFoundException extends CombinedException {
    public ProductNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
