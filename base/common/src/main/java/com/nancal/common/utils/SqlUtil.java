package com.nancal.common.utils;

import cn.hutool.core.util.StrUtil;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;

/***
 * 操作SQL语句的工具类
 *
 * @author 徐鹏军
 * @date 2022/4/11 16:30
 */
public class SqlUtil {

    private static final String PERCENT = "%";
    private static final String STAR = "*";

    /***
     * 全模糊匹配
     *
     * @param value 值
     * @author 徐鹏军
     * @date 2022/4/11 16:33
     * @return {@link String}
     */
    public static String like(String value) {
        if (StrUtil.isBlank(value)) {
            throw new ServiceException(ErrorCode.E_10);
        }
        return PERCENT + value + PERCENT;
    }
    /***
     * 右模糊匹配
     *
     * @param value 值
     * @author 徐鹏军
     * @date 2022/4/11 16:33
     * @return {@link String}
     */
    public static String likeRight(String value) {
        if (StrUtil.isBlank(value)) {
           throw new ServiceException(ErrorCode.E_10);
        }
        return value + PERCENT;
    }
    /***
     * 左模糊匹配
     *
     * @param value 值
     * @author 徐鹏军
     * @date 2022/4/11 16:33
     * @return {@link String}
     */
    public static String likeLeft(String value) {
        if (StrUtil.isBlank(value)) {
            throw new ServiceException(ErrorCode.E_10);
        }
        return PERCENT + value;
    }
    /***
     * 根据*通配符模糊匹配
     *
     * @param value 值
     * @author 徐鹏军
     * @date 2022/4/11 16:33
     * @return {@link String}
     */
    public static String likeStar(String value) {
        if (StrUtil.isBlank(value)) {
            throw new ServiceException(ErrorCode.E_10);
        }
        // 全模糊匹配
        if (StrUtil.startWith(value,STAR) && !StrUtil.endWith(value,STAR)) {
            return PERCENT + StrUtil.removeAll(value, STAR);
        }
        if (!StrUtil.startWith(value,STAR) && StrUtil.endWith(value,STAR)) {
            return StrUtil.removeAll(value, STAR) + PERCENT;
        }
        return PERCENT + StrUtil.removeAll(value, STAR) + PERCENT;
    }
}
