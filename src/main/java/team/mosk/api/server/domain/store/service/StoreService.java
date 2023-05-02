package team.mosk.api.server.domain.store.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.exception.DuplicateCrnException;
import team.mosk.api.server.domain.store.exception.DuplicateEmailException;
import team.mosk.api.server.domain.store.exception.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.model.persist.QRCodeRepository;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final QRCodeRepository qrCodeRepository;

    private final PasswordEncoder encoder;

    private final WebClient webClient;

    private final String qrImgWidthHeight = "500x500";

    private final String qrImgPath = "/Users/hhpp1205/Documents/";

    public StoreService(StoreRepository storeRepository,
                        QRCodeRepository qrCodeRepository,
                        PasswordEncoder encoder,
                        @Qualifier("qrCodeClient") WebClient webClient) {
        this.storeRepository = storeRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.encoder = encoder;
        this.webClient = webClient;
    }

    public StoreResponse create(Store store) {
        if(storeRepository.existsByEmail(store.getEmail())) {
            throw new DuplicateEmailException("이미 등록된 이메일 입니다.");
        }

        if(storeRepository.existsByCrn(store.getCrn())){
            throw new DuplicateCrnException("이미 등록된 사업자등록번호 입니다.");
        }

        store.setEncodePassword(encoder.encode(store.getPassword()));

        return StoreResponse.of(storeRepository.save(store));
    }

    public StoreResponse update(Long storeId, StoreUpdateRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        store.update(request);
        return StoreResponse.of(store);
    }

    public void delete(Long storeId) {
        storeRepository.deleteById(storeId);
    }

    public void createQRCode(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        String path = "http://localhost:3030/" + storeId;

        String uuidFileName = createUUIDFileName();

        callCreateQRCode(path, uuidFileName);

        qrCodeRepository.save(new QRCode(path + uuidFileName, store));
    }


    private String createUUIDFileName() {
        String uuidFileName = UUID.randomUUID().toString();
        int idx = uuidFileName.indexOf('-');
        uuidFileName = uuidFileName.substring(0, idx) + ".png";
        return uuidFileName;
    }
    private void callCreateQRCode(String path, String uuidFileName) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("cht", "qr")
                        .queryParam("chs", qrImgWidthHeight)
                        .queryParam("chl", path)
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .subscribe(bytes -> {
                    try (FileOutputStream outputStream = new FileOutputStream(new File(qrImgPath + uuidFileName))) {
                        outputStream.write(bytes);
                    } catch (IOException e) {
                        log.error("error={}", e);
                        throw new RuntimeException("Error writing file", e);
                    }
                });
    }

}
