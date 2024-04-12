package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MfgStationPr;
import org.springframework.stereotype.Component;

@Component
public class Gte4MfgStationPrFactory {
    public Gte4MfgStationPr create(){
        return new Gte4MfgStationPr();
    }
}