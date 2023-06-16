package team.mosk.api.server.domain.store.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mosk.api.server.domain.subscribe.model.persist.QSubscribe;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.Optional;

import static team.mosk.api.server.domain.store.model.persist.QStore.*;
import static team.mosk.api.server.domain.subscribe.model.persist.QSubscribe.*;

@Repository
@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CustomUserDetails> findUserDetailsByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(CustomUserDetails.class,
                                store.id,
                                store.email,
                                store.password,
                                store.subscribe.endDate))
                        .from(store)
                        .leftJoin(store.subscribe, subscribe)
                        .where(store.email.eq(email))
                        .fetchOne());
    }

    @Override
    public Optional<CustomUserDetails> findUserDetailsById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(CustomUserDetails.class,
                                store.id,
                                store.email,
                                store.password,
                                store.subscribe.endDate))
                        .from(store)
                        .leftJoin(store.subscribe, subscribe)
                        .where(store.id.eq(id))
                        .fetchOne());
    }


}
