package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ChangeOrderResp 更改单的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ChangeOrderResp implements Serializable{

    @ApiModelProperty(value="设计更改单响应",required=false)
    private ChangeAfterRLResp changeAfterRLResp;

    @ApiModelProperty(value="通知单位集合",required=false)
    private List<Gte4NoticeOrgEntryResp> noticeListResp;

    @ApiModelProperty(value="发放单位集合",required=false)
    private List<Gte4DistOrgEntryResp> distListResp;

}