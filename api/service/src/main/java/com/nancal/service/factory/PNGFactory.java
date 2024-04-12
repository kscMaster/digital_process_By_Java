package com.nancal.service.factory;

import com.nancal.service.bo.PNG;
import org.springframework.stereotype.Component;

@Component
public class PNGFactory {
    public PNG create(){
        return new PNG();
    }
}