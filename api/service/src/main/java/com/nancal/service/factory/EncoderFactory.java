package com.nancal.service.factory;

import com.nancal.service.bo.Encoder;
import org.springframework.stereotype.Component;

@Component
public class EncoderFactory {
    public Encoder create() {
        return new Encoder();
    }
}
