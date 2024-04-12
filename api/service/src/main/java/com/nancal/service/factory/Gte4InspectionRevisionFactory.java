package com.nancal.service.factory;

import com.nancal.service.bo.Gte4InspectionRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4InspectionRevisionFactory {
    public Gte4InspectionRevision create(){
        return new Gte4InspectionRevision();
    }
}