package com.nancal.service.factory;

import com.nancal.service.bo.DocumentRevision;
import org.springframework.stereotype.Component;

@Component
public class DocumentRevisionFactory {
    public DocumentRevision create(){
        return new DocumentRevision();
    }
}