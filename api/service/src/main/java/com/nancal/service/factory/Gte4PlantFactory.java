package com.nancal.service.factory;

import com.nancal.service.bo.Gte4Plant;
import org.springframework.stereotype.Component;

@Component
public class Gte4PlantFactory {
    public Gte4Plant create(){
        return new Gte4Plant();
    }
}