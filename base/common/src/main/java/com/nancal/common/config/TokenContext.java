package com.nancal.common.config;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TokenContext {

    private static final ThreadLocal<TokenInfo> contextHolder = new ThreadLocal();

    public static TokenInfo getToken(){
        TokenInfo tokenInfo = contextHolder.get();
        if(tokenInfo == null){
           return null;
        }
        return tokenInfo;
    }

    public static void setToken(TokenInfo tokenInfo){
        contextHolder.set(tokenInfo);
    }
}


