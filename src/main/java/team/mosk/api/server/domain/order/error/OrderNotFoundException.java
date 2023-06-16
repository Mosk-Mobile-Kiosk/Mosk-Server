package team.mosk.api.server.domain.order.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class OrderNotFoundException extends CombinedException {
    public OrderNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
