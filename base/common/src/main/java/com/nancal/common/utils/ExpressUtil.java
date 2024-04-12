package com.nancal.common.utils;

import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.StringReader;
import java.io.StringWriter;

public class ExpressUtil {

    private static Logger logger = LoggerFactory.getLogger(ExpressUtil.class);

    public static String parseObj(String src, Object obj){
        try {
            StringWriter result = new StringWriter();
            Template t = new Template("src", new StringReader(src), new Configuration());
            t.process(obj, result);
            return result.toString();
        }catch (Exception e){
            logger.error("模版转换异常，src={},msg={}",src,e.getMessage());
            logger.warn("模版转换异常",e);
            throw new ServiceException(ErrorCode.ERROR);
        }
    }

}
