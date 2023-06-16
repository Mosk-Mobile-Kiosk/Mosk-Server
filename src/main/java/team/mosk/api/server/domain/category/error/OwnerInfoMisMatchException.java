package team.mosk.api.server.domain.category.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class OwnerInfoMisMatchException extends CombinedException {
    public OwnerInfoMisMatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
