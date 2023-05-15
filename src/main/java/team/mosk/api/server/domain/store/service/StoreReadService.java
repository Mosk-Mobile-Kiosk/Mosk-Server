package team.mosk.api.server.domain.store.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import team.mosk.api.server.domain.store.dto.BusinessCheckRequest;
import team.mosk.api.server.domain.store.dto.BusinessCheckResponse;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.exception.DuplicateCrnException;
import team.mosk.api.server.domain.store.exception.DuplicateEmailException;
import team.mosk.api.server.domain.store.exception.QRCodeNotFoundException;
import team.mosk.api.server.domain.store.exception.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.model.persist.QRCodeRepository;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class StoreReadService {

    private final StoreRepository storeRepository;

    private final QRCodeRepository qrCodeRepository;

    private final WebClient webClient;

    private final String gongGongApiKey;


    public StoreReadService(StoreRepository storeRepository,
                            QRCodeRepository qrCodeRepository,
                            @Qualifier("gongGongDataClient") WebClient webClient,
                            @Value("${gongGongData.apiKey}") String gongGongApiKey) {
        this.storeRepository = storeRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.webClient = webClient;
        this.gongGongApiKey = gongGongApiKey;
    }

    public StoreResponse findById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        return StoreResponse.of(store);
    }

    public void emailDuplicateCheck(String email) {
        if(storeRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("사용중인 이메일입니다.");
        }
    }

    public void businessRegistrationCheck(BusinessCheckRequest request) {
        ArrayList<BusinessCheckRequest> bcrList = new ArrayList<>();
        bcrList.add(request);

        ResponseEntity<BusinessCheckResponse> response = webClient.post()
                .bodyValue(bcrList)
                .retrieve()
                .toEntity(BusinessCheckResponse.class)
                .block();

        if (response.getStatusCode() != HttpStatus.OK || !isBusinessCheck(response.getBody().getValid_msg())) {
            throw new DuplicateCrnException("이미 존재하는 사업자등록 번호 입니다.");
        }
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
