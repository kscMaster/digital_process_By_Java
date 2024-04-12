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

import javax.validation.constraints.NotBlank;


@ApiModel(value = "Gte4NoticeOrgEntry 通知单位信息 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4NoticeOrgEntryReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="通知单位",required=false)
    @Length(max = 128, message = "通知单位长度超过了最大长度限制",groups = {UpdateGroup.class})
    @NotBlank(message = "通知单位不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4NoticeOrg;

    @ApiModelProperty(value="通知单位ID",required=false)
    @Length(max = 64, message = "通知单位ID长度超过了最大长度限制",groups = {UpdateGroup.class})
    @NotBlank(message = "通知单位ID不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4NoticeOrgId;

    @ApiModelProperty(value="通知时间",required=false,example="1994-03-07")
    private LocalDateTime gte4NoticeDate;

    @ApiModelProperty(value="已阅",required=false)
    private Boolean gte4Check;

    @ApiModelProperty(value="已阅时间",required=false,example="1994-03-07")
    private LocalDateTime gte4CheckTime;

    public String getObjectType(){
        return "Gte4NoticeOrgEntry";
    }

}