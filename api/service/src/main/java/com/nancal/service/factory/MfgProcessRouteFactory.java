package com.nancal.service.factory;

import com.nancal.service.bo.MfgProcessRoute;
import org.springframework.stereotype.Component;

@Component
public class MfgProcessRouteFactory {
    public MfgProcessRoute create(){
        return new MfgProcessRoute();
    }
}