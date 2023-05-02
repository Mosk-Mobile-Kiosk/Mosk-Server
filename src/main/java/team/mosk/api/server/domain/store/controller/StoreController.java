package team.mosk.api.server.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.store.dto.*;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.service.StoreReadService;
import team.mosk.api.server.domain.store.service.StoreService;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StoreController {

    private final StoreService storeService;
    private final StoreReadService storeReadService;

    @PostMapping("public/stores")
    public ResponseEntity<StoreResponse> create(@Validated @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(request.toEntity()));
    }

    @GetMapping("stores")
    public ResponseEntity<StoreResponse> findById(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(storeReadService.findById(customUserDetails.getId()));
    }

    @GetMapping("public/stores/email-check/{email}")
    public ResponseEntity<Void> emailCheck(@PathVariable String email) {
        if (storeReadService.emailDuplicateCheck(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("public/stores/business-registration")
    public ResponseEntity<Void> businessRegistrationCheck(@RequestParam("crn") String crn,
                                                          @RequestParam("foundedDate") String foundedDate,
                                                          @RequestParam("ownerName") String ownerName) {
        BusinessCheckRequest request = new BusinessCheckRequest(crn, foundedDate.replaceAll("-", ""), ownerName);

        if (storeReadService.businessRegistrationCheck(request)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/stores")
    public ResponseEntity<StoreResponse> update(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                @Validated @RequestBody StoreUpdateRequest request) {
        return ResponseEntity.ok(storeService.update(customUserDetails.getId(), request));
    }

    @DeleteMapping("/stores")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        storeService.delete(customUserDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stores/qrcode")
    public ResponseEntity<byte[]> createQRCode(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        storeService.createQRCode(customUserDetails.getId());
        QRCode qrCode = storeReadService.getQRCode(customUserDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeToByte(qrCode));
    }

    @GetMapping(value = "/stores/qrcode")
    public ResponseEntity<byte[]> getQRCode(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        QRCode qrCode = storeReadService.getQRCode(customUserDetails.getId());
        return ResponseEntity.ok()
               .contentType(MediaType.IMAGE_PNG)
               .body(qrCodeToByte(qrCode));
    }

    private byte[] qrCodeToByte(QRCode qrCode) {
        try {
            return Files.readAllBytes(new File(qrCode.getPath()).toPath());
        } catch (IOException e) {
            log.error("error={}", e);
            throw new RuntimeException(e);
        }
    }

}
