package team.mosk.api.server.domain.store.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.error.DuplicateCrnException;
import team.mosk.api.server.domain.store.error.DuplicateEmailException;
import team.mosk.api.server.domain.store.error.QrCodeAlreadyExistsException;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
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

    private final String qrImgWidthHeight;

    private final String qrImgSavedPath;

    private final String storePageUrl;

    public StoreService(StoreRepository storeRepository,
                        QRCodeRepository qrCodeRepository,
                        PasswordEncoder encoder,
                        @Qualifier("qrCodeClient") WebClient webClient,
                        @Value("${filePath}") String qrImgSavedPath,
                        @Value("${storePageUrl}") String storePageUrl,
                        @Value("${qrImgWidthHeight}") String qrImgWidthHeight) {
        this.storeRepository = storeRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.encoder = encoder;
        this.webClient = webClient;
        this.qrImgSavedPath = qrImgSavedPath;
        this.storePageUrl = storePageUrl;
        this.qrImgWidthHeight = qrImgWidthHeight;
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

        if(store.getQrCode() != null) {
            throw new QrCodeAlreadyExistsException("이미 Qrcode가 존재합니다.");
        }

        String uuidFileName = createUUIDFileName();

        callCreateQRCode(storePageUrl + storeId, uuidFileName);

        qrCodeRepository.save(new QRCode(qrImgSavedPath + "/" + uuidFileName, store));
    }


    private String createUUIDFileName() {
        String uuidFileName = UUID.randomUUID().toString();
        int idx = uuidFileName.indexOf('-');
        uuidFileName = uuidFileName.substring(0, idx) + ".png";
        return uuidFileName;
    }
    private void callCreateQRCode(String path, String uuidFileName) {
        byte[] bytes = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("cht", "qr")
                        .queryParam("chs", qrImgWidthHeight)
                        .queryParam("chl", path)
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        try (FileOutputStream outputStream = new FileOutputStream(new File(qrImgSavedPath + "/" + uuidFileName))) {
            outputStream.write(bytes);
        } catch (IOException e) {
            log.error("error={}", e);
            throw new RuntimeException("Error writing file", e);
        }
    }

}
