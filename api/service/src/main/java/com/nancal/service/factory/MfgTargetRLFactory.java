package com.nancal.service.factory;

import com.nancal.service.bo.MfgTargetRL;
import org.springframework.stereotype.Component;

@Component
public class MfgTargetRLFactory {
    public MfgTargetRL create(){
        return new MfgTargetRL();
    }
}