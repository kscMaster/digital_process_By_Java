package com.nancal.service.factory;

import com.nancal.service.bo.Gte4TestForm;
import org.springframework.stereotype.Component;

@Component
public class Gte4TestFormFactory {
    public Gte4TestForm create(){
        return new Gte4TestForm();
    }
}