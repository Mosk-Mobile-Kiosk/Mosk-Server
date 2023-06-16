package team.mosk.api.server.domain.options.option.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class OptionNotFoundException extends CombinedException {
    public OptionNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
