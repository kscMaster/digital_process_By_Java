package com.nancal.service.factory;

import com.nancal.service.bo.FileStorage;
import org.springframework.stereotype.Component;

@Component
public class FileStorageFactory {
    public FileStorage create(){
        return new FileStorage();
    }
}