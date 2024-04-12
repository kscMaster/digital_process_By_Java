package com.nancal.service.factory;

import com.nancal.service.bo.IncludeRL;
import org.springframework.stereotype.Component;

@Component
public class IncludeRLFactory {
    public IncludeRL create(){
        return new IncludeRL();
    }
}