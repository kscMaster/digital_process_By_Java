package com.nancal.service.factory;

import com.nancal.service.bo.Gte4CleanupMachining;
import org.springframework.stereotype.Component;

@Component
public class Gte4CleanupMachiningFactory {
    public Gte4CleanupMachining create(){
        return new Gte4CleanupMachining();
    }
}