package team.mosk.api.server.domain.store.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team.mosk.api.server.domain.store.dto.SignUpRequest;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.service.StoreReadService;
import team.mosk.api.server.domain.store.service.StoreService;
import team.mosk.api.server.domain.store.util.WithAuthUser;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static team.mosk.api.server.domain.auth.util.GivenAuth.*;
import static team.mosk.api.server.domain.store.util.GivenStore.*;

@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    StoreService storeService;

    @MockBean
    StoreReadService storeReadService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("가게 등록")
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

    @Test
    @DisplayName("내정보 조회")
    @WithAuthUser
    void findById() throws Exception {
        //given
        StoreResponse storeResponse = givenStoreResponse();

        //when
        when(storeReadService.findById(1L)).thenReturn(storeResponse);

        mockMvc.perform(get("/api/v1/stores"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 중복확인(사용가능)")
    void emailCheck() throws Exception {
        when(storeReadService.emailDuplicateCheck(any())).thenReturn(false);

        mockMvc.perform(get("/api/v1/public/stores/email-check/{email}", GIVEN_EMAIL))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 중복확인(사용불가)")
    void emailCheckFail() throws Exception {
        when(storeReadService.emailDuplicateCheck(any())).thenReturn(true);

        mockMvc.perform(get("/api/v1/public/stores/email-check/{email}", GIVEN_EMAIL))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("사업자등록번호 중복확인(사용가능)")
    void businessRegistrationCheck() throws Exception {
        when(storeReadService.businessRegistrationCheck(any())).thenReturn(false);

        mockMvc.perform(get("/api/v1/public/stores/business-registration")
                        .param("crn", GIVEN_CRN)
                        .param("foundedDate", GIVEN_FOUNDED_DATE)
                        .param("ownerName", GIVEN_OWNERNAME))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("사업자등록번호 중복확인(사용불가)")
    void businessRegistrationCheckFail() throws Exception {
        when(storeReadService.businessRegistrationCheck(any())).thenReturn(true);

        mockMvc.perform(get("/api/v1/public/stores/business-registration")
                        .param("crn", GIVEN_CRN)
                        .param("foundedDate", GIVEN_FOUNDED_DATE)
                        .param("ownerName", GIVEN_OWNERNAME))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("가게정보 변경")
    @WithAuthUser
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
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원탈퇴")
    @WithAuthUser
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/v1/stores"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("QRCode 생성")
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