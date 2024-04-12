package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "ImexportTask 导入导出 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ImexportTask extends WorkspaceObject  implements Serializable{

    public static final String OPERATION_TYPE = "operationType";
    public static final String APP_CODE = "appCode";
    public static final String OPERATION_OBJECT_TYPE = "operationObjectType";
    public static final String TASK_NAME = "taskName";
    public static final String FILE_NAME = "fileName";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String OPERATION_STATUS = "operationStatus";
    public static final String EXPORT_URL = "exportUrl";
    public static final String ERROR_LOG = "errorLog";


    @ApiModelProperty(value="操作类型",required=false)
    private String operationType;

    @ApiModelProperty(value="应用Code",required=false)
    private String appCode;

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
    private String operationStatus;

    @ApiModelProperty(value="导出路径",required=false)
    private String exportUrl;

    @ApiModelProperty(value="失败日志",required=false)
    private String errorLog;

    public String getObjectType(){
        return "ImexportTask";
    }

}