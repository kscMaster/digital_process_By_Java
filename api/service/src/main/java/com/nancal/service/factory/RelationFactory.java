package com.nancal.service.factory;

import com.nancal.service.bo.Relation;
import org.springframework.stereotype.Component;

@Component
public class RelationFactory {
    public Relation create(){
        return new Relation();
    }
}