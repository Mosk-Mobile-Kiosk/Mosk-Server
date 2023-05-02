package team.mosk.api.server.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import team.mosk.api.server.domain.auth.dto.AccessToken;
import team.mosk.api.server.domain.auth.dto.SignInDto;
import team.mosk.api.server.domain.auth.service.AuthService;
import team.mosk.api.server.domain.store.util.WithAuthUser;
import team.mosk.api.server.global.jwt.TokenProvider;
import team.mosk.api.server.global.jwt.dto.TokenDto;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static team.mosk.api.server.domain.auth.util.GivenAuth.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        SignInDto signInDto = new SignInDto(GIVEN_EMAIL, GIVEN_PASSWORD);
        String body = objectMapper.writeValueAsString(signInDto);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);

        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);

        //when
        when(authService.login(any())).thenReturn(tokenDto);

        mockMvc.perform(
                        post("/api/v1/public/auth")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @Test
    @DisplayName("로그아웃")
    @WithAuthUser
    void logout() throws Exception {
        //given
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);

        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);

        //when
        mockMvc.perform(
                        delete("/api/v1/auth")
                                .cookie(new Cookie("refreshToken", tokenDto.getRefreshToken())))
                .andExpect(status().isNoContent())
                .andExpect(cookie().value("refreshToken", (String) null))
                .andDo(print());
    }


    @Test
    @DisplayName("리프레쉬 토큰을 통해 엑세스 토큰 재발급")
    void reissue() throws Exception {
        //given
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);

        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);
        String body = objectMapper.writeValueAsString(tokenDto.getAccessToken());

        //when
        when(authService.reissue(any(), any())).thenReturn(AccessToken.of(tokenDto.getAccessToken()));

        mockMvc.perform(
                        post("/api/v1/public/auth/reissue")
                                .cookie(new Cookie("refreshToken", tokenDto.getRefreshToken()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated())
                .andDo(print());
    }

}