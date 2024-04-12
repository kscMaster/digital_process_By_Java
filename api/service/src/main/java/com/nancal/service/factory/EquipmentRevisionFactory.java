package com.nancal.service.factory;

import com.nancal.service.bo.EquipmentRevision;
import org.springframework.stereotype.Component;

@Component
public class EquipmentRevisionFactory {
    public EquipmentRevision create(){
        return new EquipmentRevision();
    }
}