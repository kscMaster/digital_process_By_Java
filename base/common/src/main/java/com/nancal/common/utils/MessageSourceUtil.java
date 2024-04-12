package com.nancal.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceUtil {

    private static Logger log = LoggerFactory.getLogger(MessageSourceUtil.class);

    private static MessageSource messageSource;

    @Autowired
    public void setDatastore(MessageSource messageSource) {
        MessageSourceUtil.messageSource = messageSource;
    }

    public static String getMessage(String key,Object... args){
        try {
            String result = messageSource.getMessage(key, args, LocalContext.getLocale());
            return result;
        }catch (Exception e){
            log.warn("中英文翻译不存在,key={}",key);
        }
        return key;
    }

}
