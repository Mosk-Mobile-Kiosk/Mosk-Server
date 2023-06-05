package team.mosk.api.server.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.mosk.api.server.domain.order.error.TossApiException;
import team.mosk.api.server.domain.subscribe.error.AlreadyExpiredSubscribeException;
import team.mosk.api.server.domain.subscribe.error.SubInfoNotFoundException;
import team.mosk.api.server.global.common.ApiResponse;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TossApiException.class)
    public ApiResponse<Object> tossApiException(TossApiException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SubInfoNotFoundException.class)
    public ApiResponse<Object> subInfoNotFoundException(SubInfoNotFoundException e) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExpiredSubscribeException.class)
    public ApiResponse<Object> alreadyExpiredSubscribeException(AlreadyExpiredSubscribeException e) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null);
    }
}
