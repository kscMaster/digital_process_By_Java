package com.nancal.service.factory;

import com.nancal.service.bo.ToolRevision;
import org.springframework.stereotype.Component;

@Component
public class ToolRevisionFactory {
    public ToolRevision create(){
        return new ToolRevision();
    }
}