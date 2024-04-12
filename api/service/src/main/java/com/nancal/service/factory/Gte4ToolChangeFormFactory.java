package com.nancal.service.factory;

import com.nancal.service.bo.Gte4ToolChangeForm;
import org.springframework.stereotype.Component;

@Component
public class Gte4ToolChangeFormFactory {
    public Gte4ToolChangeForm create(){
        return new Gte4ToolChangeForm();
    }
}