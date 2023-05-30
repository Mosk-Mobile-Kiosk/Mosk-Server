package team.mosk.api.server.domain.store.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.UUID;

@Slf4j
@Service
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final QRCodeRepository qrCodeRepository;
    private final QRCodeClient qrCodeClient;
    private final PasswordEncoder encoder;
    private final String qrImgSavedPath;

    public StoreService(StoreRepository storeRepository,
                        QRCodeRepository qrCodeRepository,
                        QRCodeClient qrCodeClient,
                        PasswordEncoder encoder,
                        @Value("${filePath}") String qrImgSavedPath
                        ) {
        this.storeRepository = storeRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.qrCodeClient = qrCodeClient;
        this.encoder = encoder;
        this.qrImgSavedPath = qrImgSavedPath;
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


    /**
     * 빨간줄 큐알서비스로 옮겨야함
     * @param storeId
     */
    public void createQRCode(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        if(store.getQrCode() != null) {
            throw new QrCodeAlreadyExistsException("이미 Qrcode가 존재합니다.");
        }

        String uuid = createUUIDFileName();

        qrCodeClient.callCreateQRCode(storeId, uuid);

        qrCodeRepository.save(new QRCode(qrImgSavedPath + "/" + uuid, store));
    }

    private String createUUIDFileName() {
        String uuidFileName = UUID.randomUUID().toString();
        int idx = uuidFileName.indexOf('-');
        uuidFileName = uuidFileName.substring(0, idx) + ".png";
        return uuidFileName;
    }



}
