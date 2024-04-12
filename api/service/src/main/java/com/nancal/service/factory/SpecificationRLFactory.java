package com.nancal.service.factory;

import com.nancal.service.bo.SpecificationRL;
import org.springframework.stereotype.Component;

@Component
public class SpecificationRLFactory {
    public SpecificationRL create(){
        return new SpecificationRL();
    }
}