package team.mosk.api.server.global.error.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String code;
    private String message;

    private ErrorResponse(final ErrorCode errorCode) {
        status = errorCode.getStatus();
        code = errorCode.getCode();
        message = errorCode.getMessage();
    }

    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

}
