package com.nancal.service.factory;

import com.nancal.service.bo.JT;
import org.springframework.stereotype.Component;

@Component
public class JTFactory {
    public JT create(){
        return new JT();
    }
}