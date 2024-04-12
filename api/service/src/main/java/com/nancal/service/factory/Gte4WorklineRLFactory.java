package com.nancal.service.factory;

import com.nancal.service.bo.Gte4WorklineRL;
import org.springframework.stereotype.Component;

@Component
public class Gte4WorklineRLFactory {
    public Gte4WorklineRL create(){
        return new Gte4WorklineRL();
    }
}