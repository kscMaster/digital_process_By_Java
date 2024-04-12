package com.nancal.service.factory;

import com.nancal.service.bo.MfgProcess;
import org.springframework.stereotype.Component;

@Component
public class MfgProcessFactory {
    public MfgProcess create(){
        return new MfgProcess();
    }
}