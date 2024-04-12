package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "封面的响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrontCoverResp extends WorkspaceObjectResp{
    @ApiModelProperty("生效日期")
    private LocalDateTime effectiveDate;

    @ApiModelProperty("日期")
    private LocalDate date;

    @ApiModelProperty("审核（审核人姓名-审核日期）")
    private String examine;

    @ApiModelProperty(name = "节点")
    private List<CoverNodeResp> node;

}
