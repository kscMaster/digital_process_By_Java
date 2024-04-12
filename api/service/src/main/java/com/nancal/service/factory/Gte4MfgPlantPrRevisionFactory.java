package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgPlantPrRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgPlantPrRevisionFactory {
    public Gte4MfgPlantPrRevision create(){
        return new Gte4MfgPlantPrRevision();
    }
}