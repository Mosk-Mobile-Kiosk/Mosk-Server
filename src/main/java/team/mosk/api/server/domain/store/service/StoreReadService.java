package team.mosk.api.server.domain.store.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import team.mosk.api.server.domain.store.dto.BusinessCheckRequest;
import team.mosk.api.server.domain.store.dto.BusinessCheckResponse;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.exception.QRCodeNotFoundException;
import team.mosk.api.server.domain.store.exception.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.model.persist.QRCodeRepository;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class StoreReadService {

    private final StoreRepository storeRepository;
    private final QRCodeRepository qrCodeRepository;
    private final WebClient webClient;

    public StoreReadService(StoreRepository storeRepository,
                            QRCodeRepository qrCodeRepository,
                            @Qualifier("gongGongDataClient") WebClient webClient) {
        this.storeRepository = storeRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.webClient = webClient;
    }

    public StoreResponse findById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        return StoreResponse.of(store);
    }

    public boolean emailDuplicateCheck(String email) {
        return storeRepository.existsByEmail(email);
    }

    public boolean businessRegistrationCheck(BusinessCheckRequest request) {
        ArrayList<BusinessCheckRequest> bcrList = new ArrayList<>();
        bcrList.add(request);

        ResponseEntity<BusinessCheckResponse> response = webClient.post()
                .bodyValue(bcrList)
                .retrieve()
                .toEntity(BusinessCheckResponse.class)
                .block();

        if (response.getStatusCode() == HttpStatus.OK) {
            return isBusinessCheck(response.getBody().getValid_msg());
        }

        return false;
    }

    public QRCode getQRCode(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        return qrCodeRepository.findByStore(store)
                .orElseThrow(() -> new QRCodeNotFoundException("QRCODE를 찾을 수 없습니다."));
    }

    private boolean isBusinessCheck(String resultMsg) {
        return resultMsg == "" ? true : false;
    }

}
