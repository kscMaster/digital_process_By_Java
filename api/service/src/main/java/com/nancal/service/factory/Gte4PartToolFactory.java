package com.nancal.service.factory;

import com.nancal.service.bo.Gte4PartTool;
import org.springframework.stereotype.Component;

@Component
public class Gte4PartToolFactory {
    public Gte4PartTool create(){
        return new Gte4PartTool();
    }
}