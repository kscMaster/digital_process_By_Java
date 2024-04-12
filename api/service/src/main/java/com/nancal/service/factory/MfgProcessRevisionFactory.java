package com.nancal.service.factory;

import com.nancal.service.bo.MfgProcessRevision;
import org.springframework.stereotype.Component;

@Component
public class MfgProcessRevisionFactory {
    public MfgProcessRevision create(){
        return new MfgProcessRevision();
    }
}