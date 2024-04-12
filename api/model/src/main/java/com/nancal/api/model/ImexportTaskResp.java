package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel(value = "ImexportTask 导入导出 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImexportTaskResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="操作类型",required=false)
    @JsonDict(DictConstant.IMEXPORTTASK_OPERATIONTYPE)
    private String operationType;
    @ApiModelProperty(value="应用Code",required=false)
    @JsonDict(DictConstant.IMEXPORTTASK_APPCODE)
    private String appCode;
    @JsonDict(DictConstant.OBJECT_TYPE_NAME)
    @ApiModelProperty(value="操作对象类型",required=false)
    private String operationObjectType;
    @ApiModelProperty(value="任务名称",required=false)
    private String taskName;
    @ApiModelProperty(value="文件名称",required=false)
    private String fileName;
    @ApiModelProperty(value="开始时间",required=false,example="1994-03-07")
    private LocalDateTime startDate;
    @ApiModelProperty(value="结束时间",required=false,example="1994-03-07")
    private LocalDateTime endDate;
    @ApiModelProperty(value="状态",required=false)
    @JsonDict(DictConstant.IMEXPORTTASK_OPERATIONSTATUS)
    private String operationStatus;
    @ApiModelProperty(value="导出路径",required=false)
    private String exportUrl;
    @ApiModelProperty(value="失败日志",required=false)
    private String errorLog;

    public String getObjectType(){
       return "ImexportTask";
    }

}