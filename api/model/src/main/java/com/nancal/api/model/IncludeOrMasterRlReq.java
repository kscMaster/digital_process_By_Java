package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName IncludeOrMasterRlReq
 * @Description TODO
 * @Author fuming
 * @Date 2022/4/2 15:03
 * @Version 1.0
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "IncludeRL 或者Master 包含关系 的请求")
public class IncludeOrMasterRlReq implements Serializable {

    @NotBlank(message = "ID不能为空")
    @ApiModelProperty(value = "主键id", required = true)
    private String uid;

    @ApiModelProperty(value = "对象类型")
    private String objectType;

    @ApiModelProperty(value = "是否查询自己的，true是，false否")
    private boolean isOwner = true;

    @ApiModelProperty(value = "左对象id")
    private String leftObject;
    @ApiModelProperty(value = "左对象类行")
    private String leftObjectType;
}
