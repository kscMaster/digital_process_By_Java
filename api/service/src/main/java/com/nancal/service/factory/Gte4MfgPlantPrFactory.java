package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgPlantPr;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgPlantPrFactory {
    public Gte4MfgPlantPr create(){
        return new Gte4MfgPlantPr();
    }
}