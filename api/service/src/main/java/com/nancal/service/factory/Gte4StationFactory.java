package com.nancal.service.factory;

import com.nancal.service.bo.Gte4Station;
import org.springframework.stereotype.Component;

@Component
public class Gte4StationFactory {
    public Gte4Station create(){
        return new Gte4Station();
    }
}