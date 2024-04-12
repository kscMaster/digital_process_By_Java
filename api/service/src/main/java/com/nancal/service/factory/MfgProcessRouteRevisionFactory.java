package com.nancal.service.factory;

import com.nancal.service.bo.MfgProcessRouteRevision;
import org.springframework.stereotype.Component;

@Component
public class MfgProcessRouteRevisionFactory {
    public MfgProcessRouteRevision create(){
        return new MfgProcessRouteRevision();
    }
}