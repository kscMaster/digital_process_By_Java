package com.nancal.service.factory;

import com.nancal.service.bo.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentFactory {
    public Equipment create(){
        return new Equipment();
    }
}