package com.nancal.service.factory;

import com.nancal.service.bo.ItemRevision;
import org.springframework.stereotype.Component;

@Component
public class ItemRevisionFactory {
    public ItemRevision create(){
        return new ItemRevision();
    }
}