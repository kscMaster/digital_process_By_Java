package com.nancal.service.factory;

import com.nancal.service.bo.AttachRL;
import com.nancal.service.bo.Dataset;
import org.springframework.stereotype.Component;

@Component
public class DataSetFactory {
    public Dataset create(){
        return new Dataset();
    }
}