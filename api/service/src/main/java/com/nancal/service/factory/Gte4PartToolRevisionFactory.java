package com.nancal.service.factory;

import com.nancal.service.bo.Gte4PartToolRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4PartToolRevisionFactory {
    public Gte4PartToolRevision create(){
        return new Gte4PartToolRevision();
    }
}