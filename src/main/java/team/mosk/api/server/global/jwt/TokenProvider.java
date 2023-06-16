package team.mosk.api.server.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import team.mosk.api.server.global.jwt.dto.TokenDto;
import team.mosk.api.server.global.security.principal.CustomUserDetails;
import team.mosk.api.server.global.security.principal.CustomUserDetailsService;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final String secret;
    private final long accessTokenValidityInMillisecond;
    private final long refreshTokenValidityInMillisecond;
    private final CustomUserDetailsService customUserDetailsService;

    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.accessToken-validity-in-seconds}") long accessTokenValidityInMillisecond,
                         @Value("${jwt.refreshToken-validity-in-seconds}")long refreshTokenValidityInMillisecond,
                         CustomUserDetailsService customUserDetailsService) {
        this.secret = secret;
        this.accessTokenValidityInMillisecond = accessTokenValidityInMillisecond * 1000;
        this.refreshTokenValidityInMillisecond = refreshTokenValidityInMillisecond * 1000;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(String email, Authentication authentication) {
        long now = (new Date()).getTime();

        String accessToken = Jwts.builder()
                .claim("email", email.toString())
                .setExpiration(new Date(now + accessTokenValidityInMillisecond))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenValidityInMillisecond))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.of(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = String.valueOf(claims.get("email"));

        CustomUserDetails principal = customUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        }catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        }catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 토큰입니다.");
        }catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


}
