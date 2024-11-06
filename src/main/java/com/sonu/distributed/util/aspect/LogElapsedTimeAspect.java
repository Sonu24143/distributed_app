package com.sonu.distributed.util.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogElapsedTimeAspect {

    @Around("@annotation(com.sonu.distributed.util.LogElapsedTime)")
    public Object logElapsedTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        final Object proceed = proceedingJoinPoint.proceed();
        final long totalTime = (System.currentTimeMillis() - start);
        log.warn("Method [{}], time taken [{}] ms.", proceedingJoinPoint.getSignature(), totalTime);
        return proceed;
    }
}
