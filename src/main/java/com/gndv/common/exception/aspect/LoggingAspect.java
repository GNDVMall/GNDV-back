package com.gndv.common.exception.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
        // 포인트컷: RestController가 적용된 클래스
    }

    @Before("controller()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering in Method : {}", joinPoint.getSignature().getName());
        log.info("Class Name : {}", joinPoint.getSignature().getDeclaringTypeName());
        log.info("Arguments : {}", (Object[]) joinPoint.getArgs());
        log.info("Target class : {}", joinPoint.getTarget().getClass().getName());
    }

    @AfterThrowing(pointcut = "controller()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("An exception has been thrown in {}()", joinPoint.getSignature().getName());
        log.error("Cause : {}", ex.getCause());
    }
}
