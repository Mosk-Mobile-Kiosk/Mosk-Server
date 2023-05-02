package team.mosk.api.server.domain.product.model.persist.expression;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

import static team.mosk.api.server.domain.product.model.persist.QProduct.*;

public enum ProductExpression {
    EQ_CATEGORY_ID {
        @Override
        public BooleanExpression eqProductField(Long categoryId) {
            if (categoryId == null) {
                return null;
            }
            return product.category.id.eq(categoryId);
        }
    },
    EQ_STORE_ID {
        @Override
        public BooleanExpression eqProductField(Long storeId) {
            if (storeId == null) {
                return null;
            }
            return product.store.id.eq(storeId);
        }
    },

    EQ_PRODUCT_ID {
        @Override
        public BooleanExpression eqProductField(Long productId) {
            if (productId == null) {
                return null;
            }
            return product.id.eq(productId);
        }
    };

    public abstract BooleanExpression eqProductField(final Long targetId);
}
