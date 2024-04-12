package com.nancal.api.model;

import com.nancal.api.model.common.WorkBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PBomReq<T extends WorkspaceObjectReq> extends BusinessObjectReq {

//    @ApiModelProperty("bom行对象属性")
//    private BOMNodePbomReq bomNode;

    @ApiModelProperty("业务请求对象")
    private T t;

    @ApiModelProperty("标准件")
    private WorkBaseReq standard;

    @ApiModelProperty("工艺路线")
    private WorkBaseReq route;

    @ApiModelProperty("主材关系")
    private WorkBaseReq r006Process;

    @ApiModelProperty("辅材关系")
    private WorkBaseReq r006ProcessAux;


}
