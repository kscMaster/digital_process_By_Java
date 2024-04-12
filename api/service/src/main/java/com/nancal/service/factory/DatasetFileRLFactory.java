package com.nancal.service.factory;

import com.nancal.service.bo.DatasetFileRL;
import org.springframework.stereotype.Component;

@Component
public class DatasetFileRLFactory {
    public DatasetFileRL create(){
        return new DatasetFileRL();
    }
}