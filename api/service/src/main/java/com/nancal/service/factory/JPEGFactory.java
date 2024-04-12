package com.nancal.service.factory;

import com.nancal.service.bo.JPEG;
import org.springframework.stereotype.Component;

@Component
public class JPEGFactory {
    public JPEG create(){
        return new JPEG();
    }
}