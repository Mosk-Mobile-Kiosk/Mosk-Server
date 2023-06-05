package team.mosk.api.server.domain.order.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class OrderAccessDeniedException extends CombinedException {
    public OrderAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
