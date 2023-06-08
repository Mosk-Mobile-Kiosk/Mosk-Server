package team.mosk.api.server.global.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import team.mosk.api.server.domain.subscribe.error.AlreadyExpiredSubscribeException;
import team.mosk.api.server.domain.subscribe.error.SubInfoNotFoundException;
import team.mosk.api.server.global.error.exception.ErrorCode;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.time.LocalDate;

@Aspect
@Component
public class ValidSubscribeAspect {

    @Before("@annotation(team.mosk.api.server.global.aop.ValidSubscribe) || @within(team.mosk.api.server.global.aop.ValidSubscribe)")
    public void beforeExecuteRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for(Object arg : args) {
            if(arg instanceof CustomUserDetails) {
                CustomUserDetails details = (CustomUserDetails) arg;
                if(details.getPeriod().isBefore(LocalDate.now())) {
                    throw new SubInfoNotFoundException(ErrorCode.SUB_INFO_NOT_FOUND);
                } else if (details.getPeriod() == null) {
                    throw new AlreadyExpiredSubscribeException(ErrorCode.ALREADY_EXPIRED_SUBSCRIBE);
                }
            }
        }
    }
}
