package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgOperation;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgOperationFactory {
    public Gte4MfgOperation create(){
        return new Gte4MfgOperation();
    }
}