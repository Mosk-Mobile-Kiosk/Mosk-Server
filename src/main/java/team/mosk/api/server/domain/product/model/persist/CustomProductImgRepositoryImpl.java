package team.mosk.api.server.domain.product.model.persist;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mosk.api.server.domain.product.model.persist.CustomProductImgRepository;
import team.mosk.api.server.domain.product.model.persist.ProductImg;

import java.util.Optional;

import static team.mosk.api.server.domain.product.model.persist.QProductImg.*;

@Repository
@RequiredArgsConstructor
public class CustomProductImgRepositoryImpl implements CustomProductImgRepository {

    private final JPAQueryFactory query;
    @Override
    public Optional<ProductImg> findImgByProductId(final Long productId) {
        return Optional.ofNullable(query.selectFrom(productImg)
                .where(productImg.product.id.eq(productId))
                .fetchOne());
    }
}
