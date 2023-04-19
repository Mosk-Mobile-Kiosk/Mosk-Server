package team.mosk.api.server.domain.store.controller;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.store.dto.BusinessCheckRequest;
import team.mosk.api.server.domain.store.dto.MemberResponse;
import team.mosk.api.server.domain.store.dto.SignUpRequest;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.service.StoreService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<MemberResponse> create(@Validated @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(request.toEntity()));
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
    public ResponseEntity<MemberResponse> update(@Validated @RequestBody StoreUpdateRequest request) {
        // TODO: 2023/04/19  현재 사용자 정보 가져와서 처리
        return null;
    }





}
