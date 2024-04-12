package com.nancal.api.model;

import com.nancal.common.annotation.QueryField;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;


@ApiModel(value = "ImexportTask 导入导出 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImexportTaskReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="操作类型",required=false)
    @Length(max = 32)
    @QueryField(Ops.EQ)
    private String operationType;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="应用Code",required=false)
    @Length(max = 64)
    private String appCode;

    @ApiModelProperty(value="操作对象类型",required=false)
    @Length(max = 128)
    private String operationObjectType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="任务名称",required=false)
    @Length(max = 255)
    private String taskName;

    @ApiModelProperty(value="文件名称",required=false)
    @Length(max = 255)
    private String fileName;

    @ApiModelProperty(value="开始时间",required=false,example="1994-03-07")
    private LocalDateTime startDate;

    @ApiModelProperty(value="结束时间",required=false,example="1994-03-07")
    private LocalDateTime endDate;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="状态",required=false)
    @Length(max = 32)
    private String operationStatus;

    @ApiModelProperty(value="导出路径",required=false)
    @Length(max = 255)
    private String exportUrl;

    @ApiModelProperty(value="失败日志",required=false)
    @Length(max = 1024)
    private String errorLog;

    public String getObjectType(){
        return "ImexportTask";
    }

}