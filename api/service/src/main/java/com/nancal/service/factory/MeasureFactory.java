package com.nancal.service.factory;

import com.nancal.service.bo.Measure;
import org.springframework.stereotype.Component;

@Component
public class MeasureFactory {
    public Measure create(){
        return new Measure();
    }
}