package com.nancal.service.factory;

import com.nancal.service.bo.ZIP;
import org.springframework.stereotype.Component;

@Component
public class ZIPFactory {
    public ZIP create(){
        return new ZIP();
    }
}