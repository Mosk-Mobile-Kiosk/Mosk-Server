package team.mosk.api.server.global.error.exception;

import lombok.Getter;

@Getter
public class CombinedException extends RuntimeException{

    private final ErrorCode errorCode;

    public CombinedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
