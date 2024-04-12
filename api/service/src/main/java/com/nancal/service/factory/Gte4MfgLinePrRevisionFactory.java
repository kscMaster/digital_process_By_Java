package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgLinePrRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgLinePrRevisionFactory {
    public Gte4MfgLinePrRevision create(){
        return new Gte4MfgLinePrRevision();
    }
}