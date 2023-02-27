package com.softura.skillup.aspect;

import org.aspectj.lang.annotation.*;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {

    private static Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);


    @Pointcut("execution(* *(..)) &&\n" +
            "(\n" +
            "    within(com.softura.skillup..controller.*) ||\n" +
            "    within(com.softura.skillup..service.*) \n" +
            ")")

    public void logJoinPoint() {
    }

    @Before("logJoinPoint()")
    public void beforeExecutionOfMethod(JoinPoint jointPoint) {
        LOGGER.info("Enter {}.{}() with args {}",
                jointPoint.getSignature().getDeclaringTypeName(),
                jointPoint.getSignature().getName(),
                Arrays.toString(jointPoint.getArgs())
        );
    }

    @AfterReturning(pointcut = "logJoinPoint()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        LOGGER.info("Exiting from Method :" + joinPoint.getSignature().getName());
        LOGGER.info("Return value :" + result);
    }

    @AfterThrowing(pointcut = "logJoinPoint()", throwing = "result")
    public void logAfterException(JoinPoint joinPoint, Object result) {
        LOGGER.info("Exception from Method :" + joinPoint.getSignature().getName());
        LOGGER.info("Exception :" + result);
    }

}
