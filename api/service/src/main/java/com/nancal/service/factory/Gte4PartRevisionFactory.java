package com.nancal.service.factory;

import com.nancal.service.bo.Gte4PartRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4PartRevisionFactory {
    public Gte4PartRevision create(){
        return new Gte4PartRevision();
    }
}