package team.mosk.api.server.domain.subscribe.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class AlreadyExpiredSubscribeException extends CombinedException {
    public AlreadyExpiredSubscribeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
