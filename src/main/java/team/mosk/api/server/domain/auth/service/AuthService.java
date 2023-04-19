package team.mosk.api.server.domain.auth.service;

import org.springframework.stereotype.Service;
import team.mosk.api.server.domain.auth.dto.AccessToken;
import team.mosk.api.server.domain.auth.dto.SignInDto;
import team.mosk.api.server.global.jwt.dto.TokenDto;

@Service
public class AuthService {
    public TokenDto login(SignInDto request) {
        // TODO: 2023/04/19  
        return null;
    }

    public AccessToken reissue(AccessToken accessToken, String refreshToken) {
        // TODO: 2023/04/19  
        return null;
    }
}
