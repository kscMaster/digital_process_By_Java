package com.nancal.service.factory;

import com.nancal.service.bo.Gte4InspectionRL;
import org.springframework.stereotype.Component;

@Component
public class Gte4InspectionRLFactory {
    public Gte4InspectionRL create(){
        return new Gte4InspectionRL();
    }
}