package com.nancal.api.model;

import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.FidHistoryGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;


@ApiModel(value = "SearchHistory 查询条件历史 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryReq extends WorkspaceObjectReq  implements Serializable{

    @NotNull(message = "应用code",groups = {AddGroup.class,FidHistoryGroup.class})
    @ApiModelProperty(value="应用code",required=false)
    @Length(max = 128,message = "应用code超过了最大长度限制",groups = {AddGroup.class,FidHistoryGroup.class})
    private String appCode;

    @NotNull(message = "条件名称不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value="条件名称",required=false)
    @Length(max = 128,message = "条件名称超过了最大长度限制",groups = {AddGroup.class})
    private String conditionName;

    @NotNull(message = "组合json条件不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value="组合json条件",required=false)
    @Length(max = 1024,message = "组合json条件超过了最大长度限制")
    private String combinCondition;


    @NotNull(message = "条件对象类型不能为空",groups = {AddGroup.class, FidHistoryGroup.class})
    @ApiModelProperty(value="条件对象类型",required=false)
    @Length(max = 128,message = "条件对象类型长度超过了最大限制")
    private String conditionObjectType;

    public String getObjectType(){
        return "SearchHistory";
    }

}