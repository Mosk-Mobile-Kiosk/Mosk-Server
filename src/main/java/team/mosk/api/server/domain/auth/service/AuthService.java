package team.mosk.api.server.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.mosk.api.server.domain.auth.dto.AccessToken;
import team.mosk.api.server.domain.auth.dto.SignInDto;
import team.mosk.api.server.domain.store.exception.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.global.jwt.TokenProvider;
import team.mosk.api.server.global.jwt.dto.TokenDto;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;

    /**
     * findUserDetailsByEmail를 QueryDsl로 처리 예정
     */
    public TokenDto login(SignInDto request) {
        String password = passwordEncoder.encode(request.getPassword());

        // TODO: 2023/04/22  
        CustomUserDetails userDetails = storeRepository.findUserDetailsByEmail(request.getEmail())
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password);

        Authentication authenticate = managerBuilder.getObject().authenticate(token);

        return tokenProvider.createToken(userDetails.getId(), authenticate);
    }

    public AccessToken reissue(AccessToken accessToken, String refreshToken) {
        // TODO: 2023/04/19  
        return null;
    }
}
