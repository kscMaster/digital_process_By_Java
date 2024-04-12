package com.nancal.service.factory;

import com.nancal.service.bo.MfgOperation;
import org.springframework.stereotype.Component;

@Component
public class MfgOperationFactory {
    public MfgOperation create(){
        return new MfgOperation();
    }
}