package com.nancal.service.factory;

import com.nancal.service.bo.Gte4TechDocPdmRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4TechDocPdmRevisionFactory {
    public Gte4TechDocPdmRevision create(){
        return new Gte4TechDocPdmRevision();
    }
}