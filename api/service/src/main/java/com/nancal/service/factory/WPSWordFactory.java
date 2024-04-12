package com.nancal.service.factory;

import com.nancal.service.bo.WPSWord;
import org.springframework.stereotype.Component;

@Component
public class WPSWordFactory {
    public WPSWord create(){
        return new WPSWord();
    }
}