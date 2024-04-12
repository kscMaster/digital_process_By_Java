package com.nancal.service.factory;

import com.nancal.service.bo.Gte4OilInspectionForm;
import org.springframework.stereotype.Component;

@Component
public class Gte4OilInspectionFormFactory {
    public Gte4OilInspectionForm create(){
        return new Gte4OilInspectionForm();
    }
}