package team.mosk.api.server.domain.subscribe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.domain.subscribe.dto.SubscribeResponse;
import team.mosk.api.server.domain.subscribe.error.SubInfoNotFoundException;
import team.mosk.api.server.domain.subscribe.model.persist.Subscribe;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistory;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistoryRepository;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscribeService {

    private final StoreRepository storeRepository;
    private final SubscribeRepository subscribeRepository;
    private final SubscribeHistoryRepository subscribeHistoryRepository;

    private static final String STORE_NOT_FOUND = "가게를 찾을 수 없습니다.";
    private static final String SUB_INFO_NOT_FOUND = "구독 정보가 존재하지 않습니다.";
    public SubscribeResponse newSubscribe(final Long storeId, final Long period, final Long price) {

        if (existsSubscribe(storeId)) {
            return renewSubscribe(storeId, period, price);
        }

        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(STORE_NOT_FOUND));

        Subscribe sub = Subscribe.createEntity(findStore, period, price);

        Subscribe savedSub = subscribeRepository.save(sub);
        transferToHistory(savedSub, price);

        return SubscribeResponse.of(savedSub);
    }

    public SubscribeResponse renewSubscribe(final Long storeId, final Long period, final Long price) {
        Subscribe findSub = subscribeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new SubInfoNotFoundException(SUB_INFO_NOT_FOUND));

        if(findSub.getEndDate().isBefore(LocalDate.now())) {
            findSub.resetStartDate();
        }

        transferToHistory(findSub, price);

        findSub.renewEndDate(period);

        return SubscribeResponse.of(findSub);
    }

    public void transferToHistory(final Subscribe subscribe, final Long price) {
        SubscribeHistory newHistory = SubscribeHistory.transfer(subscribe, price);

        subscribeHistoryRepository.save(newHistory);
    }

    public boolean existsSubscribe(final Long storeId) {
        return subscribeRepository.findByStoreId(storeId).isEmpty();
    }
}
