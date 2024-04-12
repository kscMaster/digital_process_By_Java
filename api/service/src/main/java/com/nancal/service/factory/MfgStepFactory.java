package com.nancal.service.factory;

import com.nancal.service.bo.MfgStep;
import org.springframework.stereotype.Component;

@Component
public class MfgStepFactory {
    public MfgStep create(){
        return new MfgStep();
    }
}