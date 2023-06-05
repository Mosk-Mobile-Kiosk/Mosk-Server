package team.mosk.api.server.domain.store.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team.mosk.api.server.ControllerIntegrationSupport;
import team.mosk.api.server.domain.store.dto.SignUpRequest;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.util.WithAuthUser;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static team.mosk.api.server.domain.auth.util.GivenAuth.*;
import static team.mosk.api.server.domain.store.util.GivenStore.*;


class StoreControllerTest extends ControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("가게를 등록할 수 있다.(회원가입)")
    @Test
    void create() throws Exception {
        //given
        SignUpRequest signUpRequest = givenStoreSignUpRequest();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                post("/api/v1/public/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("가게 등록 시 이메일은 이메일 형식이어야 한다.")
    @Test
    void createWithPlainText() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("test")
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                        post("/api/v1/public/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("이메일 형식이여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게 등록 시 이메일은 필수 값이다.")
    @Test
    void createWithoutEmail() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                        post("/api/v1/public/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("이메일은 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게 등록 시 비밀번호는 필수 값이다.")
    @Test
    void createWithoutPassword() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(GIVEN_EMAIL)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                        post("/api/v1/public/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게 등록 시 가게이름은 필수 값이다.")
    @Test
    void createWithoutStoreName() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                        post("/api/v1/public/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("가게이름은 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게 등록 시 사업주이름은 필수 값이다.")
    @Test
    void createWithoutOwnerName() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                        post("/api/v1/public/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("사업주이름은 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게 등록 시 핸드폰 번호는 필수 값입니다.")
    @Test
    void createWithoutCall() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                        post("/api/v1/public/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("휴대폰 번호는 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게 등록 시 주소는 필수 값이다.")
    @Test
    void createWithoutAddress() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .crn(GIVEN_CRN)
                .build();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                        post("/api/v1/public/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("주소는 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게 등록 시 사업자등록번호는 필수 값이다.")
    @Test
    void createWithoutCrn() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .build();
        String body = objectMapper.writeValueAsString(signUpRequest);

        //when
        when(storeService.create(signUpRequest.toEntity())).thenReturn(StoreResponse.of(signUpRequest.toEntity()));

        mockMvc.perform(
                        post("/api/v1/public/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("사업자등록 번호는 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("내정보를 조회할 수 있다.")
    @WithAuthUser
    @Test
    void findById() throws Exception {
        //given
        StoreResponse storeResponse = givenStoreResponse();

        //when
        when(storeReadService.findById(1L)).thenReturn(storeResponse);

        mockMvc.perform(get("/api/v1/stores"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("이메일 중복확인을 할 수 있다.")
    @Test
    void emailCheck() throws Exception {
        mockMvc.perform(get("/api/v1/public/stores/email-check/{email}", GIVEN_EMAIL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("사업자등록번호 중복확인을 할 수 있다.")
    @Test
    void businessRegistrationCheck() throws Exception {
        mockMvc.perform(get("/api/v1/public/stores/business-registration")
                        .param("crn", GIVEN_CRN)
                        .param("foundedDate", GIVEN_FOUNDED_DATE)
                        .param("ownerName", GIVEN_OWNERNAME))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("가게정보를 변경할 수 있다.")
    @WithAuthUser
    @Test
    void update() throws Exception {
        //given
        StoreResponse storeResponse = givenStoreResponse();

        StoreUpdateRequest storeUpdateRequest = givenStoreUpdateRequest();
        String body = objectMapper.writeValueAsString(storeUpdateRequest);

        //when
        when(storeService.update(any(), any())).thenReturn(storeResponse);

        mockMvc.perform(put("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("가게정보 변경 시 가게이름은 필수이다.")
    @WithAuthUser
    @Test
    void updateWithoutStoreName() throws Exception {
        //given
        StoreUpdateRequest storeUpdateRequest = StoreUpdateRequest.builder()
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .build();
        String body = objectMapper.writeValueAsString(storeUpdateRequest);

        //when
        mockMvc.perform(put("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("가게이름은 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게정보 변경 시 사업주이름은 필수이다.")
    @WithAuthUser
    @Test
    void updateWithoutOwnerName() throws Exception {
        //given
        StoreUpdateRequest storeUpdateRequest = StoreUpdateRequest.builder()
                .storeName(GIVEN_STORENAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .build();
        String body = objectMapper.writeValueAsString(storeUpdateRequest);

        //when
        mockMvc.perform(put("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("사업주이름은 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게정보 변경 시 핸드폰번호는 필수이다.")
    @WithAuthUser
    @Test
    void updateWithoutCall() throws Exception {
        //given
        StoreUpdateRequest storeUpdateRequest = StoreUpdateRequest.builder()
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .address(GIVEN_ADDRESS)
                .build();
        String body = objectMapper.writeValueAsString(storeUpdateRequest);

        //when
        mockMvc.perform(put("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("휴대폰 번호는 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("가게정보 변경 시 주소는 필수이다.")
    @WithAuthUser
    @Test
    void updateWithoutAddress() throws Exception {
        //given
        StoreUpdateRequest storeUpdateRequest = StoreUpdateRequest.builder()
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .build();
        String body = objectMapper.writeValueAsString(storeUpdateRequest);

        //when
        mockMvc.perform(put("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("주소는 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원탈퇴를 할 수 있다.")
    @WithAuthUser
    @Test
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/v1/stores"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("가게의 페이지로 가는 QRCode 생성을 할 수 있다.")
    @WithAuthUser
    void createQRCode(@TempDir Path tempDir) throws Exception {
        //given
        Path testFilePath = tempDir.resolve("image.png");
        Files.write(testFilePath, "test".getBytes());

        QRCode qrCode = givenQRCode(testFilePath.toString());

        //when
        when(storeReadService.getQRCode(any())).thenReturn(qrCode);

        mockMvc.perform(post("/api/v1/stores/qrcode"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("QRCode 조회")
    @WithAuthUser
    void getQRCode(@TempDir Path tempDir) throws Exception {
        //given
        Path testFilePath = tempDir.resolve("image.png");
        Files.write(testFilePath, "test".getBytes());

        QRCode qrCode = givenQRCode(testFilePath.toString());

        //when
        when(storeReadService.getQRCode(any())).thenReturn(qrCode);

        mockMvc.perform(get("/api/v1/stores/qrcode"))
                .andExpect(status().isOk())
                .andDo(print());
    }

}