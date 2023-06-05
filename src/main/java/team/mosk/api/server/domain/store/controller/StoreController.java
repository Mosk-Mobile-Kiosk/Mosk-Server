package team.mosk.api.server.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import team.mosk.api.server.global.common.ApiResponse;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("public/stores")
    public ApiResponse<StoreResponse> create(@Validated @RequestBody SignUpRequest request) {
        return ApiResponse.of(HttpStatus.CREATED, storeService.create(request.toEntity()));
    }

    @GetMapping("stores")
    public ApiResponse<StoreResponse> findById(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.ok(storeReadService.findById(customUserDetails.getId()));
    }


    @GetMapping("public/stores/email-check/{email}")
    public ApiResponse<Void> emailCheck(@PathVariable String email) {
        storeReadService.emailDuplicateCheck(email);
        return ApiResponse.ok();
    }

    @GetMapping("public/stores/business-registration")
    public ApiResponse<Void> businessRegistrationCheck(@RequestParam("crn") String crn,
                                                       @RequestParam("foundedDate") String foundedDate,
                                                       @RequestParam("ownerName") String ownerName) {
        BusinessCheckRequest request =
                BusinessCheckRequest.of(crn.replaceAll("-", ""), foundedDate.replaceAll("-", ""), ownerName);
        storeReadService.businessRegistrationCheck(request);
        return ApiResponse.ok();
    }

    @PutMapping("/stores")
    public ApiResponse<StoreResponse> update(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                @Validated @RequestBody StoreUpdateRequest request) {
        return ApiResponse.ok(storeService.update(customUserDetails.getId(), request));
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/stores")
    public ApiResponse<Void> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        storeService.delete(customUserDetails.getId());
        return ApiResponse.of(HttpStatus.NO_CONTENT, null);
    }

    @PostMapping("/stores/qrcode")
    public ResponseEntity<byte[]> createQRCode(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws InterruptedException {
        storeService.createQRCode(customUserDetails.getId());
        QRCode qrCode = storeReadService.getQRCode(customUserDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeToByte(qrCode));
    }

    @GetMapping("/stores/qrcode")
    public ResponseEntity<byte[]> getQRCode(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        QRCode qrCode = storeReadService.getQRCode(customUserDetails.getId());
        return ResponseEntity.ok()
               .contentType(MediaType.IMAGE_PNG)
               .body(qrCodeToByte(qrCode));
    }

    private byte[] qrCodeToByte(QRCode qrCode) {
        try {
            log.info("path : {}", qrCode.getPath());
            return Files.readAllBytes(new File(qrCode.getPath()).toPath());
        } catch (IOException e) {
            log.error("error={}", e);
            throw new RuntimeException(e);
        }
    }

}
