package team.mosk.api.server.global.jwt.exception;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class TokenNotFoundException extends CombinedException {
    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
