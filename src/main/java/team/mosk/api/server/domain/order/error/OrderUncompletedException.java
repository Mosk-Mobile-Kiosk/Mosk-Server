package team.mosk.api.server.domain.order.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class OrderUncompletedException extends CombinedException {
    public OrderUncompletedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
