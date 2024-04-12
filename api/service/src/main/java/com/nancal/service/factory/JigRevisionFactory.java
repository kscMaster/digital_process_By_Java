package com.nancal.service.factory;

import com.nancal.service.bo.JigRevision;
import org.springframework.stereotype.Component;

@Component
public class JigRevisionFactory {
    public JigRevision create(){
        return new JigRevision();
    }
}