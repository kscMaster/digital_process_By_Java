package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgStationPrRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgStationPrRevisionFactory {
    public Gte4MfgStationPrRevision create(){
        return new Gte4MfgStationPrRevision();
    }
}