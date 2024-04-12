package com.nancal.service.factory;

import com.nancal.service.bo.Gte4CnstrProcess;
import org.springframework.stereotype.Component;

@Component
public class Gte4CnstrProcessFactory {
    public Gte4CnstrProcess create(){
        return new Gte4CnstrProcess();
    }
}