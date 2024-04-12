package com.nancal.service.factory;

import com.nancal.service.bo.BOMNode;
import org.springframework.stereotype.Component;

@Component
public class BOMNodeFactory {
    public BOMNode create(){
        return new BOMNode();
    }
}