package com.nancal.service.factory;

import com.nancal.service.bo.MSPowerPoint;
import org.springframework.stereotype.Component;

@Component
public class MSPowerPointFactory {
    public MSPowerPoint create(){
        return new MSPowerPoint();
    }
}