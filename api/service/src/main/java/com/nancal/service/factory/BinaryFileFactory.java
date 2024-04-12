package com.nancal.service.factory;

import com.nancal.service.bo.BinaryFile;
import org.springframework.stereotype.Component;

@Component
public class BinaryFileFactory {
    public BinaryFile create(){
        return new BinaryFile();
    }
}