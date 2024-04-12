package com.nancal.service.factory;

import com.nancal.service.bo.MasterRL;
import org.springframework.stereotype.Component;

@Component
public class MasterRLFactory {
    public MasterRL create(){
        return new MasterRL();
    }
}