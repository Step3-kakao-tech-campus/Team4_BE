package com.ktc.matgpt.chatgpt.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class TimerAspect {

    @Pointcut("@annotation(com.ktc.matgpt.chatgpt.annotation.Timer)")
    private void timer(){ }

    @Around("timer()")
    public Object getExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("[Timer] Timer Started.");

        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info("[Timer] Time Elapsed : {}ms", stopWatch.getTotalTimeMillis());

        return result;
    }
}
