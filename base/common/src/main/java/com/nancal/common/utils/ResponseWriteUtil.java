package com.nancal.common.utils;



import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResponseWriteUtil {

    public static void writeJson(final HttpServletResponse response, final Object data){
//        throw new RuntimeException("ddd");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(data));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }

    public static void writeStr(final HttpServletResponse response, final String str){
//        throw new RuntimeException("ddd");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/text; charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(str);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }

    public static void writeHtml(final HttpServletResponse response, final String str){
//        throw new RuntimeException("ddd");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/html; charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(str);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }

}
