package com.nancal.service.factory;

import com.nancal.service.bo.Gte4ProcessRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4ProcessRevisionFactory {
    public Gte4ProcessRevision create(){
        return new Gte4ProcessRevision();
    }
}