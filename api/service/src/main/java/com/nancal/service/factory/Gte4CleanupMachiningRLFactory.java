package com.nancal.service.factory;

import com.nancal.service.bo.Gte4CleanupMachiningRL;
import org.springframework.stereotype.Component;

@Component
public class Gte4CleanupMachiningRLFactory {
    public Gte4CleanupMachiningRL create(){
        return new Gte4CleanupMachiningRL();
    }
}