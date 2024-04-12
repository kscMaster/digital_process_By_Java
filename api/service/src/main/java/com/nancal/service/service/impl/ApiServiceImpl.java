package com.nancal.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.service.service.IApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

@Service
public class ApiServiceImpl implements IApiService {


    @Override
    public void api() {
        Map<String, Object> restControllerMap = SpringUtil.getApplicationContext().getBeansWithAnnotation(RestController.class);
        Map<String, Object> controllerMap = SpringUtil.getApplicationContext().getBeansWithAnnotation(Controller.class);
        if(CollUtil.isNotEmpty(restControllerMap)){
            if(CollUtil.isNotEmpty(controllerMap)){
                restControllerMap.putAll(controllerMap);
            }
        }else{
            restControllerMap = controllerMap;
        }
        if(CollUtil.isEmpty(restControllerMap)){
            return;
        }
        for (String s : restControllerMap.keySet()) {
            Class<?> aClass = restControllerMap.get(s).getClass();
            Api annotation = aClass.getAnnotation(Api.class);
            boolean superFlag = false;
            RequestMapping requestMapping = aClass.getAnnotation(RequestMapping.class);
            if(ObjectUtil.isEmpty(requestMapping) || ObjectUtil.isEmpty(annotation)){
                requestMapping = aClass.getSuperclass().getAnnotation(RequestMapping.class);
                if(ObjectUtil.isEmpty(requestMapping) || ObjectUtil.isEmpty(annotation)){
                    continue;
                }else{
                    superFlag = true;
                }
            }
            Method[] declaredMethods =aClass.getDeclaredMethods();
            if(superFlag){
                declaredMethods =aClass.getSuperclass().getDeclaredMethods();
            }
            for (Method declaredMethod : declaredMethods) {
                PostMapping postMapping = declaredMethod.getAnnotation(PostMapping.class);
                GetMapping getMapping = declaredMethod.getAnnotation(GetMapping.class);
                ApiOperation apiOperation = declaredMethod.getAnnotation(ApiOperation.class);
                if(postMapping != null){
                    System.out.println(annotation.tags()[0]+"-"+requestMapping.value()[0]+postMapping.value()[0]+"-"+apiOperation.value());
                }else if(getMapping != null){
                    System.out.println(annotation.tags()[0]+"-"+requestMapping.value()[0]+getMapping.value()[0]+"-"+apiOperation.value());
                }
            }
        }
    }
}
