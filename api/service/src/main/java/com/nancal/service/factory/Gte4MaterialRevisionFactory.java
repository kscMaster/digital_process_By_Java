package com.nancal.service.factory;

import com.nancal.service.bo.Gte4MaterialRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4MaterialRevisionFactory {
    public Gte4MaterialRevision create(){
        return new Gte4MaterialRevision();
    }
}