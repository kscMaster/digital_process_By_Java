package com.nancal.service.factory;

import com.nancal.service.bo.Gte4PlantRL;
import org.springframework.stereotype.Component;

@Component
public class Gte4PlantRLFactory {
    public Gte4PlantRL create(){
        return new Gte4PlantRL();
    }
}