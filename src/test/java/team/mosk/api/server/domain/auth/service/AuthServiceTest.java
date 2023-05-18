package team.mosk.api.server.domain.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.auth.dto.AccessToken;
import team.mosk.api.server.domain.auth.dto.SignInDto;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.global.jwt.TokenProvider;
import team.mosk.api.server.global.jwt.dto.TokenDto;
import team.mosk.api.server.global.jwt.exception.TokenNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static team.mosk.api.server.domain.auth.util.GivenAuth.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PasswordEncoder encoder;

    @DisplayName("아이디와 비밀번호가 일치하면 토큰을 반환한다.")
    @Test
    void login() {
        //given
        Store store = createStore(GIVEN_EMAIL, GIVEN_PASSWORD);
        storeRepository.save(store);

        SignInDto request = SignInDto.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();

        //when
        TokenDto tokenDto = authService.login(request);

        //then
        assertThat(tokenDto.getAccessToken()).isNotBlank();
        assertThat(tokenDto.getRefreshToken()).isNotBlank();
    }

    @DisplayName("로그인 시 없는 이메일을 입력하면 StoreNotFoundException 발생한다.")
    @Test
    void loginWithUnSavedEmail() {
        //given
        Store store = createStore(GIVEN_EMAIL, GIVEN_PASSWORD);
        storeRepository.save(store);

        SignInDto request = SignInDto.builder()
                .email("error@error.com")
                .password(GIVEN_EMAIL)
                .build();

        //when //then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(StoreNotFoundException.class)
                .hasMessage("가게를 찾을 수 없습니다.");
    }

    @DisplayName("로그인 시 비밀번호가 다르면 BadCredentialsException 발생한다.")
    @Test
    void loginWithWrongPassword() {
        //given
        Store store = createStore(GIVEN_EMAIL, GIVEN_PASSWORD);
        storeRepository.save(store);

        SignInDto request = SignInDto.builder()
                .email(GIVEN_EMAIL)
                .password("Fail Password")
                .build();

        //when //then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("자격 증명에 실패하였습니다.");
    }

    @DisplayName("refreshToken 검증 후 accessToken을 재발급 한다.")
    @Test
    void reissue() {
        //given
        Store store = createStore(GIVEN_EMAIL, GIVEN_PASSWORD);
        storeRepository.save(store);

        TokenDto token = tokenProvider.createToken(GIVEN_EMAIL, null);

        //when
        AccessToken result = authService.reissue(AccessToken.of(token.getAccessToken()), token.getRefreshToken());

        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("재검증 시 refreshToken 검증에 실패 시 오류 발생한다.")
    @Test
    void reissueWithWrongRefreshToken() {
        //given
        Store store = createStore(GIVEN_EMAIL, GIVEN_PASSWORD);
        storeRepository.save(store);

        TokenDto token = tokenProvider.createToken(GIVEN_EMAIL, null);
        String refreshToken = "refreshToken";

        //when //then
        assertThatThrownBy(() -> authService.reissue(AccessToken.of(token.getAccessToken()), refreshToken))
                .isInstanceOf(TokenNotFoundException.class)
                .hasMessage("잘못된 JWT 서명입니다.");
    }

    private Store createStore(String email, String password) {
        return Store.builder()
                .email(email)
                .password(encoder.encode(password))
                .build();
    }

}