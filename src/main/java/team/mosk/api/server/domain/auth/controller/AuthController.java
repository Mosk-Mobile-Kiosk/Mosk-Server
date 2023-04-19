package team.mosk.api.server.domain.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.auth.dto.AccessToken;
import team.mosk.api.server.domain.auth.dto.SignInDto;
import team.mosk.api.server.domain.auth.service.AuthService;
import team.mosk.api.server.global.jwt.dto.TokenDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final Long refreshTokenValidityInMillisecond;

    public AuthController(@Value("${jwt.refreshToken-validity-in-seconds}") Long refreshTokenValidityInMillisecond,
                          AuthService authService) {
        this.refreshTokenValidityInMillisecond = refreshTokenValidityInMillisecond;
        this.authService = authService;
    }

    @PostMapping("/public/auth")
    public ResponseEntity<AccessToken> login(SignInDto request) {
        TokenDto tokenDto = authService.login(request);
        String refreshToken = tokenDto.getRefreshToken();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, getCookie(refreshToken).toString())
                .body(new AccessToken(tokenDto.getAccessToken()));
    }

    @DeleteMapping("/auth")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("public/auth/reissue")
    public ResponseEntity<AccessToken> reissue(@RequestBody AccessToken accessToken,
                                               @CookieValue(name = "refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.reissue(accessToken, refreshToken));
    }

    private ResponseCookie getCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenValidityInMillisecond)
                .build();
    }

}
