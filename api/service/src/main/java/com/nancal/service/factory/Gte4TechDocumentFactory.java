package com.nancal.service.factory;

import com.nancal.service.bo.Gte4TechDocument;
import org.springframework.stereotype.Component;

@Component
public class Gte4TechDocumentFactory {
    public Gte4TechDocument create(){
        return new Gte4TechDocument();
    }
}