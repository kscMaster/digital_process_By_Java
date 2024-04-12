package com.nancal.api.model;


import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.api.utils.FieldAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.util.List;


@ApiModel(value = "ItemRevision 零组件版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemRevisionResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="零组件号",required=false)
    private String itemId;
    @ApiModelProperty(value="版本号",required=false)
    @FieldAlias(name = "版本",max = 64)
    private String revisionId;
    @ApiModelProperty(value="单位",required=false)
    private String uom;
    @ApiModelProperty(value="版次",required=false)
    private String sequence;
    @ApiModelProperty(value="零件状态",required=false)
    private String itemRevState;
    @ApiModelProperty(value="是否激活",required=false)
    private Boolean active;
    @ApiModelProperty(value="代图号",required=false)
    private String partNo;

    @ApiModelProperty("自定义属性对象")
    private List<ExtraPropertyDataResp> extraPropertyDataRespList;

    @ApiModelProperty(value = "文件数据，数组形式传参")
    private List<FileAttrResp> files;

    @ApiModelProperty("true有编辑图代号的权限，false没有权限")
    private boolean hasDrawingCode;

}