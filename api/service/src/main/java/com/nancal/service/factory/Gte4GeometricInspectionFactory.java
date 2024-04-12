package com.nancal.service.factory;

import com.nancal.service.bo.Gte4GeometricInspection;
import org.springframework.stereotype.Component;

@Component
public class Gte4GeometricInspectionFactory {
    public Gte4GeometricInspection create(){
        return new Gte4GeometricInspection();
    }
}