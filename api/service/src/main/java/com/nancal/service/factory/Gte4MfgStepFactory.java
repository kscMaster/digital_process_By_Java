package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgStep;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgStepFactory {
    public Gte4MfgStep create(){
        return new Gte4MfgStep();
    }
}