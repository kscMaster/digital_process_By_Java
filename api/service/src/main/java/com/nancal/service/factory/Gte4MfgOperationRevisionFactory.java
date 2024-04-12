package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgOperationRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgOperationRevisionFactory {
    public Gte4MfgOperationRevision create(){
        return new Gte4MfgOperationRevision();
    }
}