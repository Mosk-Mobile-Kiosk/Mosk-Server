package team.mosk.api.server.domain.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.auth.dto.AccessToken;
import team.mosk.api.server.domain.auth.dto.SignInDto;
import team.mosk.api.server.domain.auth.service.AuthService;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.jwt.dto.TokenDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final int refreshTokenValidityInMillisecond;

    public AuthController(@Value("${jwt.refreshToken-validity-in-seconds}") int refreshTokenValidityInMillisecond,
                          AuthService authService) {
        this.refreshTokenValidityInMillisecond = refreshTokenValidityInMillisecond;
        this.authService = authService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/public/auth")
    public ApiResponse<AccessToken> login(@RequestBody @Validated SignInDto request, HttpServletResponse response) {
        TokenDto tokenDto = authService.login(request);
        String refreshToken = tokenDto.getRefreshToken();

        Cookie cookie = createCookie(
                refreshToken,
                "refreshToken",
                true,
                true,
                "/",
                refreshTokenValidityInMillisecond
        );

        response.addCookie(cookie);

        return ApiResponse.of(HttpStatus.CREATED, AccessToken.of(tokenDto.getAccessToken()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/auth")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ApiResponse.of(HttpStatus.NO_CONTENT, null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("public/auth/reissue")
    public ApiResponse<AccessToken> reissue(@RequestBody @Validated AccessToken accessToken,
                                            @CookieValue(name = "refreshToken") String refreshToken) {
        return ApiResponse.of(HttpStatus.CREATED, authService.reissue(accessToken, refreshToken));
    }

    private Cookie createCookie(String key, String value, boolean isHttpOnly, boolean isSecure, String path, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(isHttpOnly);
        cookie.setSecure(isSecure);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

}
