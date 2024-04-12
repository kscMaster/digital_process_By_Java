package com.nancal.api.utils.excel;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *  解析excel接口
 * @author HeWei123
 */
public interface Word{

    /**
     *  解析word接口
     * @param file 文件
     * @return
     */
    List analysis(MultipartFile file);
}
