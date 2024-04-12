package com.nancal.service.factory;

import com.nancal.service.bo.Change;
import org.springframework.stereotype.Component;

@Component
public class ChangeFactory {
    public Change create(){
        return new Change();
    }
}