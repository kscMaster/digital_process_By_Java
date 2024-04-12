package com.nancal.service.factory;

import com.nancal.service.bo.LibraryFolder;
import org.springframework.stereotype.Component;

@Component
public class LibraryFolderFactory {
    public LibraryFolder create(){
        return new LibraryFolder();
    }
}