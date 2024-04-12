package com.nancal.api.model;


import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.UpdateGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "ToolRevision 工装版本 的响应")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class ToolRevisionResp extends ItemRevisionResp  implements Serializable{
//    @ApiModelProperty(value="型号")
//    private String modelNo;
//    @ApiModelProperty(value="材料牌号")
//    private String materialTradeMark;
//    @ApiModelProperty(value="资源编号")
//    private String meterialCode;


    @ApiModelProperty(value = "主键id")
    private String uid;

    @ApiModelProperty(value="图号")
    @Length(max = 64,message = "图号长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class})
    private String partNo;

    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value = "对象名称")
    private String objectName;

    @QueryField(Ops.LIKE)
    @Length(max = 64,message = "代号号长度超过了最大限制",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "代号")
    private String itemId;

}