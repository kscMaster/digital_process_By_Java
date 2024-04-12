package com.nancal.api.model;

import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.utils.FieldAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "ItemRevision 零组件版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemRevisionReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="零组件号",required=false)
    @Length(max = 64)
    @FieldAlias(name = "ID")
    private String itemId;

    @ApiModelProperty(value="版本号",required=false)
    @Length(max = 64)
    @FieldAlias(name = "版本")
    private String revisionId;

    @ApiModelProperty(value="单位",required=false)
    @Length(max = 64)
    private String uom;

    @ApiModelProperty(value="版次",required=false)
    @Length(max = 64)
    private String sequence;

    @ApiModelProperty(value="零件状态",required=false)
    @Length(max = 64)
    private String itemRevState;

    @ApiModelProperty(value="是否激活",required=false)
    private Boolean active;

    @ApiModelProperty(value="代图号",required=false)
    @Length(max = 64)
    private String partNo;

    @ApiModelProperty(value = "文件数据，数组形式传参")
    private List<FileAttrReq> files;

    @ApiModelProperty(value = "自定义字段list", required = true)
    private List<ExtraPropertyDataReq> customFieldList;

    @ApiModelProperty(value="型号-批次-专业名称",required=false)
    private String fileName;

    @ApiModelProperty(value="导出列集合",required=true)
    private List<ColumnReq> libraryList;

    @ApiModelProperty(value="选中列集合",required=true)
    private List<String> idrequests;

}