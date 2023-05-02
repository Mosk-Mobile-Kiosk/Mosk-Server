package team.mosk.api.server.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import team.mosk.api.server.global.jwt.JwtAccessDeniedHandler;
import team.mosk.api.server.global.jwt.JwtAuthenticationEntryPoint;
import team.mosk.api.server.global.jwt.JwtFilter;
import team.mosk.api.server.global.jwt.TokenProvider;
import team.mosk.api.server.global.jwt.config.JwtSecurityConfig;

import static org.springframework.web.cors.CorsConfiguration.*;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String PUBLIC = "/api/v1/public/**";

    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.formLogin()
                .disable();

        security.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        security.exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        security.csrf().disable()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests()
                .antMatchers(PUBLIC).permitAll()
                .anyRequest().authenticated();

        security.apply(new JwtSecurityConfig(tokenProvider));

        return security.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern(ALL);
        configuration.addAllowedHeader(ALL);
        configuration.addAllowedMethod(ALL);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
