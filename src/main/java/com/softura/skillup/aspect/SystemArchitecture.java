package com.softura.skillup.aspect;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemArchitecture {
    @Pointcut("execution (* (@org.springframework.stereotype.Repository *).*(..))")
    public void repository() {
    }

    @Pointcut("execution (* (@org.springframework.stereotype.Service *).*(..))")
    public void service() {
    }

    @Pointcut("execution(pi)")
    public void controller() {
        System.out.println("In System architecture controller");
    }

    @Pointcut("@annotation(Log)")
    public void logPointcut() {
        System.out.println("@Log");
    }
}
