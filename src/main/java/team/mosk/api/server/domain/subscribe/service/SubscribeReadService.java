package team.mosk.api.server.domain.subscribe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.subscribe.dto.SubscribeHistoryResponse;
import team.mosk.api.server.domain.subscribe.dto.SubscribeResponse;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistory;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistoryRepository;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubscribeReadService {

    private final SubscribeHistoryRepository subscribeHistoryRepository;

    public List<SubscribeHistoryResponse> findAllByStoreId(final Long storeId) {
        List<SubscribeHistory> histories = subscribeHistoryRepository.findAllByStoreId(storeId);
        return histories.stream().map(SubscribeHistoryResponse::of).toList();
    }
}
