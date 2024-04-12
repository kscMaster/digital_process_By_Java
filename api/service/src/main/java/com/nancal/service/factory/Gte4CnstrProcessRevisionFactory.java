package com.nancal.service.factory;

import com.nancal.service.bo.Gte4CnstrProcessRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4CnstrProcessRevisionFactory {
    public Gte4CnstrProcessRevision create(){
        return new Gte4CnstrProcessRevision();
    }
}