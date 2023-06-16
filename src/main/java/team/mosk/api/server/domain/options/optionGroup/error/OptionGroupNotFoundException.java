package team.mosk.api.server.domain.options.optionGroup.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class OptionGroupNotFoundException extends CombinedException {
    public OptionGroupNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
