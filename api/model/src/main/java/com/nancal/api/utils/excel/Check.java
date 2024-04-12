package com.nancal.api.utils.excel;

import java.util.Map;

/**
 * 校验接口
 * @author hewei
 * @date 2022/8/15 14:13
 * @Description
 */
public interface Check {

    boolean verify(Class<?> aClass, Map<String, Object> fieldMap, int currentIndex);
}
