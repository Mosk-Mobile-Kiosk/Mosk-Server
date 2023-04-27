package team.mosk.api.server.domain.product.model.persist.expression;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

import static team.mosk.api.server.domain.product.model.persist.QProduct.*;

public enum ProductExpression {
    EQ_CATEGORY_NAME {
        @Override
        public BooleanExpression eqProductField(String categoryName) {
            if (!StringUtils.hasText(categoryName)) {
                return null;
            }
            return product.category.name.eq(categoryName);
        }
    };

    public abstract BooleanExpression eqProductField(final String keyword);
}
