package com.nancal.service.factory;

import com.nancal.service.bo.MfgOperationRevision;
import org.springframework.stereotype.Component;

@Component
public class MfgOperationRevisionFactory {
    public MfgOperationRevision create(){
        return new MfgOperationRevision();
    }
}