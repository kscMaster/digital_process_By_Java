package com.nancal.service.factory;

import com.nancal.service.bo.ChangeBeforeRL;
import org.springframework.stereotype.Component;

@Component
public class ChangeBeforeRLFactory {
    public ChangeBeforeRL create(){
        return new ChangeBeforeRL();
    }
}