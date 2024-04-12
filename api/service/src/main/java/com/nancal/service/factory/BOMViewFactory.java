package com.nancal.service.factory;

import com.nancal.service.bo.BOMView;
import org.springframework.stereotype.Component;

@Component
public class BOMViewFactory {
    public BOMView create(){
        return new BOMView();
    }
}