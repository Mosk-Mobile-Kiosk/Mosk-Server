package team.mosk.api.server.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.store.dto.BusinessCheckRequest;
import team.mosk.api.server.domain.store.dto.MemberResponse;
import team.mosk.api.server.domain.store.model.persist.Store;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    public MemberResponse create(Store store) {
        // TODO: 2023/04/19
        return null;
    }

    public boolean emailDuplicateCheck(String email) {
        // TODO: 2023/04/19
        return false;
    }

    public boolean businessRegistrationCheck(BusinessCheckRequest request) {
        // TODO: 2023/04/19  
        return false;
    }
}
