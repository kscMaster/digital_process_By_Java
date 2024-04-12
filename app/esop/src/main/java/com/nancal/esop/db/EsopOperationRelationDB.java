package com.nancal.esop.db;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EsopOperationRelationDB {

    @ApiModelProperty("零组件版本id")
    private String lezaoOpRevUid;

    @ApiModelProperty("产品零组件版本id")
    private String esopBopRevId;

    @ApiModelProperty("关联的对象版本id")
    private String lezaoRelatedObject;

    @ApiModelProperty("关联类型")
    private Integer esopRelationType;

}
