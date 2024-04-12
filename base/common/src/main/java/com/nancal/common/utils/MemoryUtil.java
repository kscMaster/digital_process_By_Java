package com.nancal.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PageUtil;

import java.util.Collections;
import java.util.List;

public class MemoryUtil {

    static {
        PageUtil.setFirstPageNo(1);
    }

    /***
     * 内存分页
     *
     * @param dataList 待分页的数据
     * @param pageIndex 第几页
     * @param pageSize 每页获取多少内容
     * @author 徐鹏军
     * @date 2022/4/14 13:20
     * @return {@link List<T>}
     */
    public static <T> List<T> page(List<T> dataList, int pageIndex, int pageSize) {
        if (CollUtil.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        int[] trans = PageUtil.transToStartEnd(pageIndex, pageSize);
        return CollUtil.sub(dataList, trans[0], trans[1]);
    }
}
