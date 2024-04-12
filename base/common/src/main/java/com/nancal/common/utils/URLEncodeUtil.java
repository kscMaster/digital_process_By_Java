package com.nancal.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLEncodeUtil {

    /**
     * 处理编码格式为js兼容模式
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeURIComponent(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("%28", "(")
                .replaceAll("%29", ")")
                .replaceAll("%27", "'")
                .replaceAll("%21", "!")
                .replaceAll("%7E", "~");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "(A=\"范广洲\") AND (O='成都信息工程大学')";
        System.out.println(URLEncoder.encode(s, "UTF-8"));
        System.out.println(encodeURIComponent(s));
    }
}
