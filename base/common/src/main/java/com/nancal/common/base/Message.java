package com.nancal.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Message implements Serializable {

    @ApiModelProperty(value = "响应码，0是成功，非0失败", example = "0")
    private int code;

    @ApiModelProperty(value = "响应描述")
    private String msg;

    public Message(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /***
     * 判断响应是否成功
     *
     * @author xupj
     * @date 2022/3/29 19:44
     * @return {@link Boolean}
     */
    public Boolean isFail() {
        return code != 0;
    }
}
