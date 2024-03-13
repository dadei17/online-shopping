package com.shop.online.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(Loggable)")
    public void executeLogging() {
    }

    @Around("executeLogging()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object returnValue = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        StringBuilder message = new StringBuilder("Method: ");
        message.append(joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            message.append("(").append(StringUtils.join(args, ",")).append(")");
        }
        message.append(", totalTime: ").append(totalTime).append("ms").append(", returning: ");
        if (returnValue instanceof Collection) {
            message.append(((Collection) returnValue).size()).append(" instance(s)");
        } else {
            message.append(returnValue.toString());
        }

        log.info(message.toString());
        return returnValue;
    }
}
