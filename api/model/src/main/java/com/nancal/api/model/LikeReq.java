package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @ClassName LikeReq
 * @Description TODO
 * @Author 拓凯
 * @Date 2022/4/13
 * @Version 1.0
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "Like 的请求")
public class LikeReq {
    @ApiModelProperty(value = "对象名称")
    @Length(max = 128, message = "名称超过了最大限制")
    private String objectName;

    @ApiModelProperty(value = "代号")
    @Length(max = 128, message = "代（图）号的长度不能大于128")
    private String itemId;
}
