package com.nancal.service.factory;

import com.nancal.service.bo.Gte4Workline;
import org.springframework.stereotype.Component;

@Component
public class Gte4WorklineFactory {
    public Gte4Workline create(){
        return new Gte4Workline();
    }
}