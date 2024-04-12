package com.nancal.api.model;

import com.nancal.common.base.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@ApiModel(value = "ChangeOrderReq 更改单信息 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderReq implements Serializable{

    @ApiModelProperty(value="设计更改单对象",required=true)
    @NotNull(message = "设计更改单对象不能为空",groups = {UpdateGroup.class})
    private ChangeAfterRLReq changeAfterRLReq;

    @ApiModelProperty(value="通知单位集合",required=false)
    private List<Gte4NoticeOrgEntryReq> noticeList;

    @ApiModelProperty(value="发放单位集合",required=false)
    private List<Gte4DistOrgEntryReq> distList;

}