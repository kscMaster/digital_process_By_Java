package com.nancal.api.model.common;

import com.nancal.api.model.WorkspaceObjectReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "Relation 工作对象 的请求VO")
@NoArgsConstructor
public abstract class RelationReq extends WorkspaceObjectReq {
    @ApiModelProperty(value = "关系类型")
    private String relationType;
    @ApiModelProperty(value = "左对象")
    private String leftObject;
    @ApiModelProperty(value = "左对象类型")
    private String leftObjectType;
    @ApiModelProperty(value = "右对象")
    private String rightObject;
    @ApiModelProperty(value = "右对象类型")
    private String rightObjectType;
}
