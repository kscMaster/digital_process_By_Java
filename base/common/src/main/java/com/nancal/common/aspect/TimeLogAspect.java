package com.nancal.common.aspect;

import cn.hutool.core.date.DateUtil;
import com.nancal.common.annotation.TimeLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.annotation.Annotation;

@Aspect
@Component
@Slf4j
public class TimeLogAspect {

    @Pointcut("@annotation(com.nancal.common.annotation.TimeLog)")
    public void timeLogPointCut(){
    }

    @Around("timeLogPointCut()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Annotation annotation = signature.getMethod().getAnnotation(TimeLog.class);
        String methodName = signature.getMethod().getName();
        TimeLog timeLogAnnotation = (TimeLog)annotation;
        Object result;
        try {
            long startTime = DateUtil.current();
            log.info("threadId={}，{}, {} start",Thread.currentThread(), methodName, timeLogAnnotation.handleName());
            result = joinPoint.proceed();
            long endTime = DateUtil.current();
            log.info("threadId={}，{}, {} end，time={}ms",Thread.currentThread(), methodName,timeLogAnnotation.handleName(),(endTime-startTime));
        }catch (Exception e){
            throw e;
        }
        return result;
    }


}
