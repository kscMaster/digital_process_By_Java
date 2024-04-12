package com.nancal.service.factory;

import com.nancal.service.bo.Tool;
import org.springframework.stereotype.Component;

@Component
public class ToolFactory {
    public Tool create(){
        return new Tool();
    }
}