package com.nancal.api.model;

import com.nancal.common.base.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;


@ApiModel(value = "Gte4DistOrgEntry 发放单位信息 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4DistOrgEntryReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="发放单位",required=false)
    @Length(max = 128, message = "发放单位长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4DistriOrg;

    @ApiModelProperty(value="发放单位ID",required=false)
    @Length(max = 64, message = "发放单位ID长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4DistriOrgId;

    @ApiModelProperty(value="单位责任人",required=false)
    @Length(max = 128, message = "单位责任人长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4OrgResponsibleName;

    @ApiModelProperty(value="单位责任人ID",required=false)
    @Length(max = 64, message = "单位责任人ID长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4OrgResponsibleId;

    @ApiModelProperty(value="责任人",required=false)
    @Length(max = 128, message = "责任人长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4ResponsibleName;

    @ApiModelProperty(value="责任人ID",required=false)
    @Length(max = 64, message = "责任人ID长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4ResponsibleId;

    @ApiModelProperty(value="在制品处理意见",required=false)
    @Length(max = 1024, message = "在制品处理意见长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4WipSuggestion;

    @ApiModelProperty(value="已制品处理意见",required=false)
    @Length(max = 1024, message = "已制品处理意见长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4ProcessedSuggestion;

    @ApiModelProperty(value="更改意见",required=false)
    @Length(max = 1024, message = "更改意见长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String gte4ChangeOpinion;

    @ApiModelProperty(value="生效时间",required=false,example="1994-03-07")
    private LocalDateTime gte4EffectiveDate;

    @ApiModelProperty(value="通知时间",required=false,example="1994-03-07")
    private LocalDateTime gte4NoticeDate;

    public String getObjectType(){
        return "Gte4DistOrgEntry";
    }

}