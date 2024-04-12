package com.nancal.service.factory;

import com.nancal.service.bo.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemFactory {
    public Item create(){
        return new Item();
    }
}