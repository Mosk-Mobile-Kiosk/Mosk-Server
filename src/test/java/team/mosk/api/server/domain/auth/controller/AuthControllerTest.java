package team.mosk.api.server.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import team.mosk.api.server.ControllerIntegrationSupport;
import team.mosk.api.server.domain.auth.dto.AccessToken;
import team.mosk.api.server.domain.auth.dto.SignInDto;
import team.mosk.api.server.domain.store.util.WithAuthUser;
import team.mosk.api.server.global.jwt.TokenProvider;
import team.mosk.api.server.global.jwt.dto.TokenDto;

import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static team.mosk.api.server.domain.auth.util.GivenAuth.*;

class AuthControllerTest extends ControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("사용자의 아이디, 비밀번호를 받아 로그인을 할 수 있다.")
    @Test
    void login() throws Exception {
        //given
        SignInDto signInDto = new SignInDto(GIVEN_EMAIL, GIVEN_PASSWORD);
        String body = objectMapper.writeValueAsString(signInDto);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);
        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);

        when(authService.login(any())).thenReturn(tokenDto);

        //when
        mockMvc.perform(
                        post("/api/v1/public/auth")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("로그인할 때 이메일은 이메일 형식이어야 한다.")
    @Test
    void loginWithPlainText() throws Exception {
        //given
        SignInDto signInDto = new SignInDto("email", GIVEN_PASSWORD);
        String body = objectMapper.writeValueAsString(signInDto);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);
        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);

        when(authService.login(any())).thenReturn(tokenDto);

        //when
        mockMvc.perform(
                        post("/api/v1/public/auth")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("이메일 형식이여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("로그인할 때 이메일은 필수 값이다.")
    @Test
    void loginWithoutEmail() throws Exception {
        //given
        SignInDto signInDto = SignInDto.builder()
                .password(GIVEN_PASSWORD)
                .build();
        String body = objectMapper.writeValueAsString(signInDto);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);
        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);

        when(authService.login(any())).thenReturn(tokenDto);

        //when
        mockMvc.perform(
                        post("/api/v1/public/auth")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("이메일은 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("로그인할 때 비밀번호는 필수 값이다.")
    @Test
    void loginWithoutPassword() throws Exception {
        //given
        SignInDto signInDto = SignInDto.builder()
                .email(GIVEN_EMAIL)
                .build();
        String body = objectMapper.writeValueAsString(signInDto);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);
        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);

        when(authService.login(any())).thenReturn(tokenDto);

        //when
        mockMvc.perform(
                        post("/api/v1/public/auth")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @DisplayName("로그아웃을 하면 refreshToken 값을 지운다.")
    @WithAuthUser
    @Test
    void logout() throws Exception {
        //given
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);

        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);

        //when
        mockMvc.perform(
                        delete("/api/v1/auth")
                                .cookie(new Cookie("refreshToken", tokenDto.getRefreshToken())))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(cookie().value("refreshToken", (String) null));
    }



    @DisplayName("refreshToken을 통해 accessToken을 재발급 한다.")
    @Test
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
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("토큰 재발급을 할 때 accessToken은 필수 값이다.")
    @Test
    void reissueWithoutAccessToken() throws Exception {
        //given
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(GIVEN_EMAIL, GIVEN_PASSWORD);

        TokenDto tokenDto = tokenProvider.createToken(GIVEN_EMAIL, token);

        AccessToken accessToken = AccessToken.of(" ");
        String body = objectMapper.writeValueAsString(accessToken);

        //when
        when(authService.reissue(any(), any())).thenReturn(AccessToken.of(tokenDto.getAccessToken()));

        mockMvc.perform(
                        post("/api/v1/public/auth/reissue")
                                .cookie(new Cookie("refreshToken", tokenDto.getRefreshToken()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("accessToken은 필수 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }



}