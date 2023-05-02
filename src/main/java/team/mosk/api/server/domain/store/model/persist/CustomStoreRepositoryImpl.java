package team.mosk.api.server.domain.store.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.Optional;

import static team.mosk.api.server.domain.store.model.persist.QStore.*;

@Repository
@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<CustomUserDetails> findUserDetailsByEmail(String email) {
        return Optional.of(
                queryFactory
                    .select(Projections.constructor(CustomUserDetails.class,
                            store.id,
                            store.email,
                            store.password))
                    .from(store)
                    .where(store.email.eq(email))
                    .fetchOne());
    }

    @Override
    public Optional<CustomUserDetails> findUserDetailsById(Long id) {
        return Optional.of(
                queryFactory
                        .select(Projections.constructor(CustomUserDetails.class,
                                store.id,
                                store.email,
                                store.password))
                        .from(store)
                        .where(store.id.eq(id))
                        .fetchOne());
    }


}
