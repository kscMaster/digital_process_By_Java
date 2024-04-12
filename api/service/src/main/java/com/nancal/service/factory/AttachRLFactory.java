package com.nancal.service.factory;

import com.nancal.service.bo.AttachRL;
import org.springframework.stereotype.Component;

@Component
public class AttachRLFactory {
    public AttachRL create(){
        return new AttachRL();
    }
}