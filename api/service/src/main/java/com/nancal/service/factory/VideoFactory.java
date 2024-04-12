package com.nancal.service.factory;

import com.nancal.service.bo.Video;
import org.springframework.stereotype.Component;

@Component
public class VideoFactory {
    public Video create(){
        return new Video();
    }
}