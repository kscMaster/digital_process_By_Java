package com.nancal.service.factory;

import com.nancal.service.bo.Gte4WorklineRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4WorklineRevisionFactory {
    public Gte4WorklineRevision create(){
        return new Gte4WorklineRevision();
    }
}