package com.nancal.service.factory;

import com.nancal.service.bo.Gte4TechDocumentRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4TechDocumentRevisionFactory {
    public Gte4TechDocumentRevision create(){
        return new Gte4TechDocumentRevision();
    }
}