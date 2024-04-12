package com.nancal.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class LocalContext {
    private static Logger log = LoggerFactory.getLogger(LocalContext.class);

    private static final ThreadLocal<Locale> contextHolder = new ThreadLocal();

    public static Locale getLocale(){
        Locale locale = contextHolder.get();
        if(locale == null){
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        return locale;
    }

    public static String getLanguage(){
        Locale locale = LocalContext.getLocale();
        return locale.getLanguage()+"_"+locale.getCountry();
    }

    public static void setLocale(Locale locale){
        log.info("设置lang={},currentThread={}", locale.getLanguage(),Thread.currentThread());
        contextHolder.set(locale);
    }
}


