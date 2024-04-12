package com.nancal.service.factory;

import com.nancal.service.bo.Text;
import org.springframework.stereotype.Component;

@Component
public class TextFactory {
    public Text create(){
        return new Text();
    }
}