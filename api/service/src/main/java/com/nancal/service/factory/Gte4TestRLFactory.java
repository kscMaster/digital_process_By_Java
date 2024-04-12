package com.nancal.service.factory;

import com.nancal.service.bo.Gte4TestRL;
import org.springframework.stereotype.Component;

@Component
public class Gte4TestRLFactory {
    public Gte4TestRL create(){
        return new Gte4TestRL();
    }
}