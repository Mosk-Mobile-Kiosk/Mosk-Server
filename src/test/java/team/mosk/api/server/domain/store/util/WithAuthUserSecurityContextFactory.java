package team.mosk.api.server.domain.store.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import team.mosk.api.server.domain.auth.util.GivenAuth;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.time.LocalDate;

import static team.mosk.api.server.domain.auth.util.GivenAuth.*;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        LocalDate period = LocalDate.now().plusYears(annotation.period());
        CustomUserDetails userDetails = new CustomUserDetails(annotation.id(), annotation.email(), annotation.password(), period);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, annotation.password(), null);

        SecurityContextHolder.getContext().setAuthentication(token);

        return SecurityContextHolder.getContext();
    }
}
