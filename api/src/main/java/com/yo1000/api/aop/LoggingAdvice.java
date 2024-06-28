package com.yo1000.api.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAdvice {
    @Around("execution(* com.yo1000.api.domain..*.*(..)) || " +
            "execution(* com.yo1000.api.infrastructure..*.*(..)) || " +
            "execution(* com.yo1000.api.presentation..*.*(..))")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass());

        if (logger.isDebugEnabled()) {
            logger.debug("args: {}", Arrays.stream(proceedingJoinPoint.getArgs())
                    .map(Objects::toString)
                    .collect(Collectors.joining(", ")));

            Object ret = proceedingJoinPoint.proceed();

            logger.debug("return: {}", ret);

            return ret;
        } else {
            return proceedingJoinPoint.proceed();
        }
    }
}
