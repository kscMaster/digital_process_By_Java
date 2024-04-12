package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@ApiModel(value = "SearchHistory 查询条件历史 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="应用code",required=false)
    private String appCode;
    @ApiModelProperty(value="条件名称",required=false)
    private String conditionName;
    @ApiModelProperty(value="组合json条件",required=false)
    private String combinCondition;
    @ApiModelProperty(value="条件对象类型",required=false)
    private String conditionObjectType;
    public String getObjectType(){
       return "SearchHistory";
    }

}