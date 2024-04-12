package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;


@ApiModel(value = "Gte4InspectionRevision 检验项目导入 的响应")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Gte4ImportRevisionResp implements Serializable{

    @ApiModelProperty(value="成功数",required=false)
    private int okNum;

    @ApiModelProperty(value="失败数",required=false)
    private int failNum;

    @ApiModelProperty(value="所有数",required=false)
    private int allNum;

    @ApiModelProperty(value="失败描述",required=false)
    private String msg;

    @ApiModelProperty(value="回显的uid",required=false)
    private String uid;

    @ApiModelProperty(value="回显的类型",required=false)
    private String objectType;

}