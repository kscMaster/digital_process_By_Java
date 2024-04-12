package com.nancal.service.factory;

import com.nancal.service.bo.Form;
import org.springframework.stereotype.Component;

@Component
public class FormFactory {
    public Form create(){
        return new Form();
    }
}