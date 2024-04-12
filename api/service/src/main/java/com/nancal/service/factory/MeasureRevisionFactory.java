package com.nancal.service.factory;

import com.nancal.service.bo.MeasureRevision;
import org.springframework.stereotype.Component;

@Component
public class MeasureRevisionFactory {
    public MeasureRevision create(){
        return new MeasureRevision();
    }
}