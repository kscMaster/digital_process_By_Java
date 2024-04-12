package com.nancal.service.factory;

import com.nancal.service.bo.Gte4NonDestructiveInspection;
import org.springframework.stereotype.Component;

@Component
public class Gte4NonDestructiveInspectionFactory {
    public Gte4NonDestructiveInspection create(){
        return new Gte4NonDestructiveInspection();
    }
}