package com.nancal.dictsyndata.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


/**
 * @author hewei
 * @date 2022/7/14 9:41
 * @Description
 */
@Aspect
@Order(-10)
@Component
@Slf4j
public class DataSourceAspect {


    /**
     * 切入点：service类的方法上(这个包的子包及所有包的里面的以Service结尾的类的任意方法名任意参数的方法，都讲被切到)
     */
    @Pointcut("@annotation(com.nancal.dictsyndata.config.DataSourceSwitch)")
    public void pointCut(){}

    @Before("pointCut()")
    public void around(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        final Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations){
            if (annotation instanceof  DataSourceSwitch){
                DataSourceSwitch dataSource = (DataSourceSwitch) annotation;
                DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());

            }
        }
    }

    @After("pointCut()")
    public void after(){
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

}
