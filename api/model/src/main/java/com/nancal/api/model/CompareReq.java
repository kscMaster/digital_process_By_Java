package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;


@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "BOM对比获取数据请求")
public class CompareReq {

    @ApiModelProperty(value = "左对象uid",required = true)
    @NotBlank(message = "左对象id不能为空")
    private String leftObject;

    @ApiModelProperty(value = "左对象类型",required = true)
    @NotBlank(message = "左对象类型不能为空")
    private String leftObjectType;

    @ApiModelProperty(value = "左对象是否快照",required = false)
    private boolean leftIsSnapshot=false;

    @ApiModelProperty(value = "左对象快照id",required = false)
    private String leftBomSnapshotUid;

    @ApiModelProperty(value = "右对象uid",required = true)
    @NotBlank(message = "右对象id不能为空")
    private String rightObject;

    @ApiModelProperty(value = "右对象类型",required = true)
    @NotBlank(message = "右对象类型不能为空")
    private String rightObjectType;

    @ApiModelProperty(value = "右对象是否快照",required = false)
    private boolean rightIsSnapshot=false;

    @ApiModelProperty(value = "左对象快照id",required = false)
    private String rightBomSnapshotUid;

}
