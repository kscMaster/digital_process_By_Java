package com.nancal.service.factory;

import com.nancal.service.bo.Gte4ChangeNotice;
import org.springframework.stereotype.Component;

@Component
public class Gte4ChangeNoticeFactory {
    public Gte4ChangeNotice create(){
        return new Gte4ChangeNotice();
    }
}