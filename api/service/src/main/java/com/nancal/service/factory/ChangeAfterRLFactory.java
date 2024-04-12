package com.nancal.service.factory;

import com.nancal.service.bo.ChangeAfterRL;
import org.springframework.stereotype.Component;

@Component
public class ChangeAfterRLFactory {
    public ChangeAfterRL create(){
        return new ChangeAfterRL();
    }
}