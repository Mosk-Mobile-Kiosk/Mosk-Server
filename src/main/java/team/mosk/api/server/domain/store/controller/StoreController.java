package team.mosk.api.server.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.store.dto.BusinessCheckRequest;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.dto.SignUpRequest;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.service.StoreService;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<StoreResponse> create(@Validated @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(request.toEntity()));
    }
    @GetMapping("stores")
    public ResponseEntity<StoreResponse> findById(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.findById(customUserDetails.getId()));
    }

    @GetMapping("/stores/email-check/{email}")
    public ResponseEntity<Void> emailCheck(@PathVariable String email) {
        if (storeService.emailDuplicateCheck(email)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/stores/business-registration")
    public ResponseEntity<Void> businessRegistrationCheck(@RequestParam("crn") String crn,
                                                          @RequestParam("ownerName") String ownerName,
                                                          @RequestParam("foundedDate") LocalDate foundedDate) {
        BusinessCheckRequest request = new BusinessCheckRequest(crn, ownerName, foundedDate);

        if (storeService.businessRegistrationCheck(request)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/stores")
    public ResponseEntity<StoreResponse> update(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                @Validated @RequestBody StoreUpdateRequest request) {
        return ResponseEntity.ok(storeService.update(customUserDetails.getId(), request));
    }

    @DeleteMapping("/stores")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        storeService.delete(customUserDetails.getId());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/stores/qrcode")
    public ResponseEntity<byte[]> getQRCode(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(storeService.getQRCode());
    }

}
