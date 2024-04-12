package com.nancal.service.factory;

import com.nancal.service.bo.HomeFolder;
import org.springframework.stereotype.Component;

@Component
public class HomeFolderFactory {
    public HomeFolder create(){
        return new HomeFolder();
    }
}