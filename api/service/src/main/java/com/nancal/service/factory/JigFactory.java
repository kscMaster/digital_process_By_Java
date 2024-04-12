package com.nancal.service.factory;

import com.nancal.service.bo.Jig;
import org.springframework.stereotype.Component;

@Component
public class JigFactory {
    public Jig create(){
        return new Jig();
    }
}