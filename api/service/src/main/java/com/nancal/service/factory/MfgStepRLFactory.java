package com.nancal.service.factory;

import com.nancal.service.bo.MfgStepRL;
import org.springframework.stereotype.Component;

@Component
public class MfgStepRLFactory {
    public MfgStepRL create(){
        return new MfgStepRL();
    }
}