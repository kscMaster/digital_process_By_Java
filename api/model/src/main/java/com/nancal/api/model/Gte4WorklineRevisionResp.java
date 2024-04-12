package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4WorklineRevision 线体版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4WorklineRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="序号",required=false)
    private String gte4orderNo;
    @ApiModelProperty(value="所属车间",required=false)
    private String gte4Workshop;
    @ApiModelProperty(value="资产编号",required=false)
    private String gte4AssetNum;
    @ApiModelProperty(value="区域",required=false)
    private String gte4Workarea;
    @JsonDict(DictConstant.GTE4_DEPARTMENT_NAME)
    @ApiModelProperty(value="所属部门",required=false)
    private String gte4DepartmentName;
    @ApiModelProperty(value="线体能力",required=false)
    private String gte4LineCapacity;
    @ApiModelProperty(value="线体类型",required=false)
    @JsonDict(DictConstant.GTE4_LINE_TYPE)
    private String gte4LineType;

}