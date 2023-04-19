package team.mosk.api.server.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.store.dto.BusinessCheckRequest;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.model.persist.Store;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    public StoreResponse create(Store store) {
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

    public StoreResponse update(Long storeId, StoreUpdateRequest request) {
        // TODO: 2023/04/19
        return null;
    }

    public void delete(Long storeId) {
        // TODO: 2023/04/19  
    }

    public byte[] getQRCode() {
        // TODO: 2023/04/19  
        return null;
    }

    public StoreResponse findById(Long storeId) {
        // TODO: 2023/04/19  
        return null;
    }
}
