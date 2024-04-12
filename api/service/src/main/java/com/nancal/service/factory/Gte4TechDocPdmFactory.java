package com.nancal.service.factory;

import com.nancal.service.bo.Gte4TechDocPdm;
import org.springframework.stereotype.Component;

@Component
public class Gte4TechDocPdmFactory {
    public Gte4TechDocPdm create(){
        return new Gte4TechDocPdm();
    }
}