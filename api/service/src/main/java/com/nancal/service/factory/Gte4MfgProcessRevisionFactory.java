package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgProcessRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgProcessRevisionFactory {
    public Gte4MfgProcessRevision create(){
        return new Gte4MfgProcessRevision();
    }
}