package team.mosk.api.server.global.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import team.mosk.api.server.domain.subscribe.error.AlreadyExpiredSubscribe;
import team.mosk.api.server.domain.subscribe.error.SubInfoNotFoundException;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.time.LocalDate;

@Aspect
@Component
public class ValidSubscribeAspect {

    final String SUB_INFO_NOT_FOUND = "구독 정보를 찾을 수 없습니다.";
    final String ALREADY_EXPIRED_SUB = "이미 만료된 구독입니다.";

    @Before("@annotation(team.mosk.api.server.global.aop.ValidSubscribe) || @within(team.mosk.api.server.global.aop.ValidSubscribe)")
    public void beforeExecuteRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for(Object arg : args) {
            if(arg instanceof CustomUserDetails) {
                CustomUserDetails details = (CustomUserDetails) arg;
                if(details.getPeriod().isBefore(LocalDate.now())) {
                    throw new SubInfoNotFoundException(SUB_INFO_NOT_FOUND);
                } else if (details.getPeriod() == null) {
                    throw new AlreadyExpiredSubscribe(ALREADY_EXPIRED_SUB);
                }
            }
        }
    }
}
