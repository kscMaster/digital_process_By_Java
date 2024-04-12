package com.nancal.service.factory;

import com.nancal.service.bo.MSWord;
import org.springframework.stereotype.Component;

@Component
public class MSWordFactory {
    public MSWord create(){
        return new MSWord();
    }
}