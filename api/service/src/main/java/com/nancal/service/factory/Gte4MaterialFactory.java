package com.nancal.service.factory;

import com.nancal.service.bo.Gte4Material;
import org.springframework.stereotype.Component;

@Component
public class Gte4MaterialFactory {
    public Gte4Material create(){
        return new Gte4Material();
    }
}