package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgLinePr;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgLinePrFactory {
    public Gte4MfgLinePr create(){
        return new Gte4MfgLinePr();
    }
}