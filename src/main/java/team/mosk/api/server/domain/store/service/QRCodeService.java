package team.mosk.api.server.domain.store.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class QRCodeService {

    private final WebClient webClient;
    private final String qrImgWidthHeight;
    private final String qrImgSavedPath;
    private final String storePageUrl;

    public QRCodeService(
            @Qualifier("qrCodeClient") WebClient webClient,
            @Value("${qrImgWidthHeight}")String qrImgWidthHeight,
            @Value("${filePath}")String qrImgSavedPath,
            @Value("${storePageUrl}") String storePageUrl
    ) {
        this.webClient = webClient;
        this.qrImgWidthHeight = qrImgWidthHeight;
        this.qrImgSavedPath = qrImgSavedPath;
        this.storePageUrl = storePageUrl;
    }

    public void callCreateQRCode(Long storeId, String uuid) {
        byte[] bytes = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("cht", "qr")
                        .queryParam("chs", qrImgWidthHeight)
                        .queryParam("chl", storePageUrl + storeId)
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        try (FileOutputStream outputStream = new FileOutputStream(new File(qrImgSavedPath + "/" + uuid))) {
            outputStream.write(bytes);
        } catch (IOException e) {
            log.error("error={}", e);
            throw new RuntimeException("Error writing file", e);
        }
    }




}
