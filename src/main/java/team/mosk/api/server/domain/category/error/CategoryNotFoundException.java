package team.mosk.api.server.domain.category.error;

import team.mosk.api.server.global.error.exception.CombinedException;
import team.mosk.api.server.global.error.exception.ErrorCode;

public class CategoryNotFoundException extends CombinedException {
    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
