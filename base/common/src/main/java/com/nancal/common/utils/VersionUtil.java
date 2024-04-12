package com.nancal.common.utils;

import cn.hutool.core.util.StrUtil;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;

/***
 * 生成版本工具
 *
 * @author 徐鹏军
 * @date 2022/4/8 14:12
 */
public class VersionUtil {

    /***
     * 生成版本号
     *
     * @param revisionId 原版本号
     * @author 徐鹏军
     * @date 2022/4/8 14:19
     * @return {@link String}
     */
    public static String createRevisionId(String revisionId) {
        if (StrUtil.isBlank(revisionId)) {
            return "001";
        }
        int value = Integer.parseInt(revisionId);
        if (value >= 999) {
            throw new ServiceException(ErrorCode.FAIL,"版本生成超过最大值");
        }
        value += 1;
        if (Integer.toString(value).length() == 1) {
            return "00" + value;
        }
        if (Integer.toString(value).length() == 2) {
            return "0" + value;
        }
        return Integer.toString(value);
    }
    /***
     * 生成版次
     *
     * @param sequence 原版次
     * @author 徐鹏军
     * @date 2022/4/8 14:19
     * @return {@link String}
     */
    public static String createSequence(String sequence) {
        if (StrUtil.isBlank(sequence)) {
            return "00001";
        }
        int value = Integer.parseInt(sequence);
        if (value >= 99999) {
            throw new ServiceException(ErrorCode.FAIL,"版次生成超过最大值");
        }
        value += 1;
        if (Integer.toString(value).length() == 1) {
            return "0000" + value;
        }
        if (Integer.toString(value).length() == 2) {
            return "000" + value;
        }
        if (Integer.toString(value).length() == 3) {
            return "00" + value;
        }
        if (Integer.toString(value).length() == 4) {
            return "0" + value;
        }
        return Integer.toString(value);
    }
}
