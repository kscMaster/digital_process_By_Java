package com.nancal.service.factory;

import com.nancal.service.bo.Gte4Process;
import org.springframework.stereotype.Component;

@Component
public class Gte4ProcessFactory {
    public Gte4Process create(){
        return new Gte4Process();
    }
}