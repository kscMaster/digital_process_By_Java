package com.nancal.service.factory;

import com.nancal.service.bo.Folder;
import org.springframework.stereotype.Component;

@Component
public class FolderFactory {
    public Folder create(){
        return new Folder();
    }
}