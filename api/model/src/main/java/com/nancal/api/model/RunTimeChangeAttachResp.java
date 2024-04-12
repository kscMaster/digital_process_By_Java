package com.nancal.api.model;

import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class RunTimeChangeAttachResp extends BusinessObjectResp {

    @ApiModelProperty("总页数")
    private Integer tNum = 1;

    @ApiModelProperty("当前第几页")
    private Integer cNum = 1;

    @ApiModelProperty("更改单关联的发放单位中最小的生效时间")
    private String effictivedDate;

    @ApiModelProperty("更改单id")
    private String changeId;

    @ApiModelProperty("更改单名称")
    private String changeObjectName;

    @ApiModelProperty("更改后对象的显示名称")
    private String displayName;

    @ApiModelProperty("更改后对象的型号")
    @JsonDict(DictConstant.GTE4_INIT_MODEL)
    private String modelNo;

    @ApiModelProperty("更改后对象的代图号")
    private String partNo;

    @ApiModelProperty(value="更改后对象关系表中的同时更改的文件和资料")
    private String changeAtOnce;

    @ApiModelProperty(value="更改后对象关系表中的已制品处理意见")
    private String processedSuggestion;

    @ApiModelProperty(value="更改后对象关系表中的在制品处理意见")
    private String wipSuggestion;

    @ApiModelProperty("更改单的备注")
    private String remark;

    @ApiModelProperty("更改明细")
    private List<RunTimeChangeAttachItemResp> items = new ArrayList<>();



}
