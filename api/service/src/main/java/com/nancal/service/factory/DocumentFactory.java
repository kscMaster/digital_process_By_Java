package com.nancal.service.factory;

import com.nancal.service.bo.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentFactory {
    public Document create(){
        return new Document();
    }
}