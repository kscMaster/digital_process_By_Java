package com.nancal.service.factory;

import com.nancal.service.bo.Gte4PlantRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4PlantRevisionFactory {
    public Gte4PlantRevision create(){
        return new Gte4PlantRevision();
    }
}