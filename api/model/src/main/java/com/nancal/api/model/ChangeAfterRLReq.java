package com.nancal.api.model;

import com.nancal.common.base.AddGroup;
import com.nancal.common.base.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;


@ApiModel(value = "ChangeAfterRL 更改后 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeAfterRLReq extends RelationReq  implements Serializable{

    @ApiModelProperty(value="更改类型",required=false)
    @Length(max = 64, message = "更改类型长度超过了最大长度限制",groups = {UpdateGroup.class, AddGroup.class})
    private String changeType;

    @ApiModelProperty(value="更改原因",required=false)
    @Length(max = 1024, message = "更改原因长度超过了最大长度限制",groups = {UpdateGroup.class, AddGroup.class})
    private String changeReason;

    @ApiModelProperty(value="在制品处理意见",required=false)
    @Length(max = 1024, message = "在制品处理意见长度超过了最大长度限制",groups = {UpdateGroup.class, AddGroup.class})
    private String wipSuggestion;

    @ApiModelProperty(value="已制品处理意见",required=false)
    @Length(max = 1024, message = "已制品处理意见长度超过了最大长度限制",groups = {UpdateGroup.class, AddGroup.class})
    private String processedSuggestion;

    @ApiModelProperty(value="更改意见",required=false)
    @Length(max = 1024, message = "更改意见长度超过了最大长度限制",groups = {UpdateGroup.class, AddGroup.class})
    private String changeOpinion;

    @ApiModelProperty(value="生效时间",required=false,example="1994-03-07")
    private LocalDateTime effectiveDate;

    @ApiModelProperty(value="同时更改的文件和资料",required=false)
    @Length(max = 1024, message = "同时更改的文件和资料长度超过了最大长度限制",groups = {UpdateGroup.class, AddGroup.class})
    private String changeAtOnce;

    @ApiModelProperty(value="版本号")
    @Length(max = 64, message = "版本号长度超过了最大长度限制",groups = {UpdateGroup.class, AddGroup.class})
    private String itemRevId;

    @ApiModelProperty(value="零组件id")
    @Length(max = 64, message = "零组件id长度超过了最大长度限制",groups = {UpdateGroup.class, AddGroup.class})
    private String itemUid;


    @Override
    public String getObjectType(){
        return "ChangeAfterRL";
    }

    public String getRelationType(){
        return "ChangeAfterRL";
    }
}