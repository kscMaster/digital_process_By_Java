package com.nancal.common.constants;

/**
 * 缓存的key 常量
 *
 * @author nancal
 */

import io.swagger.annotations.ApiModelProperty;

/***
 *
 * 缓存前缀常量类
 * @author 徐鹏军
 * @date 2022/3/29 22:33
 */
public class CacheConstant {

    @ApiModelProperty("乐造数字化研发产品统一前缀")
    public static final String KEY_PRE = "lz-624:";
    @ApiModelProperty("登录TOKEN")
    public static final String LOGIN_TOKEN_KEY = KEY_PRE + "user_tokens:";
    @ApiModelProperty("字典")
    public static final String DICT_KEY = KEY_PRE + "dict:";
    @ApiModelProperty("XDM的token前缀")
    public static final String XDM_TOKEN = KEY_PRE + "xdm_token:";

}
