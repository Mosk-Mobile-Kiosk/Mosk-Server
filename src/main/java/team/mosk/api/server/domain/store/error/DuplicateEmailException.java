package team.mosk.api.server.domain.store.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class DuplicateEmailException extends CombinedException {

    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
