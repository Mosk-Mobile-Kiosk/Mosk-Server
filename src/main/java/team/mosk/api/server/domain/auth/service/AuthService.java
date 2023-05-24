package team.mosk.api.server.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import team.mosk.api.server.domain.auth.dto.AccessToken;
import team.mosk.api.server.domain.auth.dto.SignInDto;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.global.jwt.TokenProvider;
import team.mosk.api.server.global.jwt.dto.TokenDto;
import team.mosk.api.server.global.jwt.exception.TokenNotFoundException;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StoreRepository storeRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;

    public TokenDto login(SignInDto request) {
        CustomUserDetails userDetails = storeRepository.findUserDetailsByEmail(request.getEmail())
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authenticate = managerBuilder.getObject().authenticate(token);

        return tokenProvider.createToken(userDetails.getUsername(), authenticate);
    }

    public AccessToken reissue(AccessToken accessToken, String refreshToken) {
        if(!StringUtils.hasText(refreshToken) || !tokenProvider.validateToken(refreshToken)) {
            throw new TokenNotFoundException("잘못된 JWT 서명입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken.getAccessToken());
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return AccessToken.of(tokenProvider.createToken(principal.getUsername(), authentication).getAccessToken());
    }
}
