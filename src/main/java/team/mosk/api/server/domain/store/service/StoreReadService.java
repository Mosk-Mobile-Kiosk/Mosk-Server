package team.mosk.api.server.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.store.dto.BusinessCheckRequest;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.error.DuplicateEmailException;
import team.mosk.api.server.domain.store.error.QRCodeNotFoundException;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.model.persist.QRCodeRepository;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.global.client.BusinessCheckClient;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreReadService {

    private final StoreRepository storeRepository;
    private final QRCodeRepository qrCodeRepository;
    private final BusinessCheckClient businessCheckClient;

    public StoreResponse findById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND));

        return StoreResponse.of(store);
    }

    public void emailDuplicateCheck(String email) {
        if(storeRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public void businessRegistrationCheck(BusinessCheckRequest request) {
        ArrayList<BusinessCheckRequest> bcrList = new ArrayList<>();
        bcrList.add(request);

        businessCheckClient.callBusinessRegistrationCheck(bcrList);
    }

    public QRCode getQRCode(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND));

        return qrCodeRepository.findByStore(store)
                .orElseThrow(() -> new QRCodeNotFoundException(ErrorCode.QR_CODE_NOT_FOUND));
    }


    public boolean exitQRCode(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND));

        if(store.getQrCode() == null) {
            return false;
        }
        return true;
    }
}
