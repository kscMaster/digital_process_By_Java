package com.nancal.service.factory;

import com.nancal.service.bo.Gte4Part;
import org.springframework.stereotype.Component;

@Component
public class Gte4PartFactory {
    public Gte4Part create(){
        return new Gte4Part();
    }
}