package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel(value = "Gte4NoticeOrgEntry 通知单位信息 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4NoticeOrgEntryResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="通知单位",required=false)
    private String gte4NoticeOrg;
    @ApiModelProperty(value="通知单位ID",required=false)
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