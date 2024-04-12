package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgProcess;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgProcessFactory {
    public Gte4MfgProcess create(){
        return new Gte4MfgProcess();
    }
}