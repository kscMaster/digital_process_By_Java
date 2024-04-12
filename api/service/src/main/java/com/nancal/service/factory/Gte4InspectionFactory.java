package com.nancal.service.factory;

import com.nancal.service.bo.Gte4Inspection;
import org.springframework.stereotype.Component;

@Component
public class Gte4InspectionFactory {
    public Gte4Inspection create(){
        return new Gte4Inspection();
    }
}