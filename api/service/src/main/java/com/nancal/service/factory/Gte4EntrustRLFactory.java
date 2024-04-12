package com.nancal.service.factory;

import com.nancal.service.bo.Gte4EntrustRL;
import org.springframework.stereotype.Component;

@Component
public class Gte4EntrustRLFactory {
    public Gte4EntrustRL create(){
        return new Gte4EntrustRL();
    }
}