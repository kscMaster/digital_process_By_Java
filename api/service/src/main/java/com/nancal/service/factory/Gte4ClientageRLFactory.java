package com.nancal.service.factory;

import com.nancal.service.bo.Gte4ClientageRL;
import org.springframework.stereotype.Component;

@Component
public class Gte4ClientageRLFactory {
    public Gte4ClientageRL create(){
        return new Gte4ClientageRL();
    }
}