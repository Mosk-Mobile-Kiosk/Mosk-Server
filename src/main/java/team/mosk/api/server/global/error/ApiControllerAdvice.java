package team.mosk.api.server.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.mosk.api.server.domain.order.error.PaymentGatewayException;
import team.mosk.api.server.domain.subscribe.error.AlreadyExpiredSubscribeException;
import team.mosk.api.server.domain.subscribe.error.SubInfoNotFoundException;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;
import team.mosk.api.server.global.error.exception.ErrorResponse;

import static team.mosk.api.server.global.error.exception.ErrorCode.*;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }

    @ExceptionHandler(CombinedException.class)
    public ResponseEntity<ErrorResponse> handleCombinedException (CombinedException e) {
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }
}
