package com.nancal.esop.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

public class EsopUseUtil {

    /***
     * 生成指定长度的字符串，并转成大写
     *
     * @param length 长度
     * @author 徐鹏军
     * @date 2022/8/9 20:01
     * @return {@link String}
     */
    public static String randomStringUpper(int length) {
        return randomStringLower(length).toUpperCase();
    }
    /***
     * 生成指定长度的字符串，并转成小写
     *
     * @param length 长度
     * @author 徐鹏军
     * @date 2022/8/9 20:01
     * @return {@link String}
     */
    public static String randomStringLower(int length) {
        return RandomUtil.randomString(RandomUtil.BASE_CHAR,length);
    }
    /***
     * 转换版本表现形式
     *
     * @param revisionId 乐造的版本号
     * @author 徐鹏军
     * @date 2022/8/9 20:02
     * @return {@link String}
     */
    public static String versionChar(String revisionId) {
        if (StrUtil.isBlank(revisionId)) {
            return "A";
        }
        int value = Integer.parseInt(revisionId);
        if (value <= 0) {
            return "A";
        }
        if (value <= 26) {
           return String.valueOf(RandomUtil.BASE_CHAR.toCharArray()[value - 1]).toUpperCase();
        }
        value = value % 26;
        return versionChar(value + StrUtil.EMPTY);
    }
}
