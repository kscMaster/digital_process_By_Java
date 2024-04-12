package com.nancal.service.factory;

import com.nancal.service.bo.RenderingRL;
import org.springframework.stereotype.Component;

@Component
public class RenderingRLFactory {
    public RenderingRL create(){
        return new RenderingRL();
    }
}