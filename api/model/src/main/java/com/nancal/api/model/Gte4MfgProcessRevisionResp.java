package com.nancal.api.model;


import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgProcessRevision 工艺版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgProcessRevisionResp extends MfgProcessRevisionResp  implements Serializable{
    @ApiModelProperty(value="产品型代号",required=false)
    private String gte4ProductNo;
    @JsonDict(DictConstant.GTE4_PRODUCTNAME)
    @ApiModelProperty(value="产品名称",required=false)
    private String gte4ProductName;
    @ApiModelProperty(value="部组件名称",required=false)
    private String gte4PartName;
    @ApiModelProperty(value="部组件图号",required=false)
    private String gte4PartNo;
    @ApiModelProperty(value="特性分类",required=false)
    private String gte4Type;
    @JsonDict(DictConstant.GTE4_PHASE)
    @ApiModelProperty(value="阶段",required=false)
    private String gte4Phase;
    @ApiModelProperty(value="编制单位",required=false)
    private String ownerGroupName;
    @ApiModelProperty(value="分区",required=false)
    @JsonDict(DictConstant.GTE4_PARTITION)
    private String gte4partition;

//    public String getObjectType(){
//       return "Gte4MfgProcessRevision";
//    }

}