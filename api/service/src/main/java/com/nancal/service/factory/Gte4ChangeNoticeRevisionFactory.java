package com.nancal.service.factory;

import com.nancal.service.bo.Gte4ChangeNoticeRevision;
import org.springframework.stereotype.Component;

@Component
public class Gte4ChangeNoticeRevisionFactory {
    public Gte4ChangeNoticeRevision create(){
        return new Gte4ChangeNoticeRevision();
    }
}