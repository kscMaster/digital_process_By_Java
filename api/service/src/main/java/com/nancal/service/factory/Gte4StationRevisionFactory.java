package com.nancal.service.factory;

import com.nancal.service.bo.Gte4StationRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4StationRevisionFactory {
    public Gte4StationRevision create(){
        return new Gte4StationRevision();
    }
}