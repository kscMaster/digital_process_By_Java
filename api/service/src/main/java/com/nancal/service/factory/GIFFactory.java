package com.nancal.service.factory;

import com.nancal.service.bo.GIF;
import org.springframework.stereotype.Component;

@Component
public class GIFFactory {
    public GIF create(){
        return new GIF();
    }
}