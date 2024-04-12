package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel(value = "AuxiliaryMaterial 辅料 的请求")
@Data
@NoArgsConstructor
public class ExportBOMReq {
    @ApiModelProperty(value = "主键id", required = true)
    @NotBlank(message = "主键ID不能为空")
    private String uid;
    @NotBlank(message = "类型不能为空")
    @ApiModelProperty(value = "类型", required = true)
    private String objectType;
    @ApiModelProperty(value = "需要导出的列", required = true)
    private List<ColumnReq> columnReqList;
    @ApiModelProperty(value = "导出标志", required = true)
    private boolean exportFalg;
}
