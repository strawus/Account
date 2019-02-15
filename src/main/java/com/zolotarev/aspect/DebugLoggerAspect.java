package com.zolotarev.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Debugging business logic.
 * Only for development environment
 */
@Aspect
@Component
@Profile("dev")
public class DebugLoggerAspect {

    /**
     * Debugging classes in com.zolotarev.account and all sub-packages
     */
    @Around("within(com.zolotarev.account..*)")
    public Object debug(final ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        try {
            final Object result = joinPoint.proceed();
            log.debug("{}({}) returned {}", joinPoint.getSignature().getName(), joinPoint.getArgs(), result);
            return result;
        } catch (Throwable e) {
            log.debug("{}({}) threw {}(message={})", joinPoint.getSignature().getName(), joinPoint.getArgs(), e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }
}
