package com.nancal.service.factory;

import com.nancal.service.bo.LibraryFolderRL;
import org.springframework.stereotype.Component;

@Component
public class LibraryFolderRLFactory {
    public LibraryFolderRL create(){
        return new LibraryFolderRL();
    }
}