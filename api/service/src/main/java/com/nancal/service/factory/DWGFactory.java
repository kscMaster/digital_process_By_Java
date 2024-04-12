package com.nancal.service.factory;

import com.nancal.service.bo.DWG;
import org.springframework.stereotype.Component;

@Component
public class DWGFactory {
    public DWG create(){
        return new DWG();
    }
}