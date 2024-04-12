package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;
import java.time.LocalDateTime;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "imexport_task")
@ApiModel(value = "ImexportTask 导入导出 的Entity")
public class ImexportTaskEntity extends WorkspaceObjectEntity implements Serializable{

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
    @Column(name = "operation_type", length = 32)
    private String operationType;

    @ApiModelProperty(value="应用Code",required=false)
    @Column(name = "app_code", length = 64)
    private String appCode;

    @ApiModelProperty(value="操作对象类型",required=false)
    @Column(name = "operation_object_type", length = 128)
    private String operationObjectType;

    @ApiModelProperty(value="任务名称",required=false)
    @Column(name = "task_name", length = 255)
    private String taskName;

    @ApiModelProperty(value="文件名称",required=false)
    @Column(name = "file_name", length = 255)
    private String fileName;

    @ApiModelProperty(value="开始时间",required=false,example="1994-03-07")
    @Column(name = "start_date", length = 0)
    private LocalDateTime startDate;

    @ApiModelProperty(value="结束时间",required=false,example="1994-03-07")
    @Column(name = "end_date", length = 0)
    private LocalDateTime endDate;

    @ApiModelProperty(value="状态",required=false)
    @Column(name = "operation_status", length = 32)
    private String operationStatus;

    @ApiModelProperty(value="导出路径",required=false)
    @Column(name = "export_url", length = 255)
    private String exportUrl;

    @ApiModelProperty(value="失败日志",required=false)
    @Column(name = "error_log", length = 1024)
    private String errorLog;

    public String getObjectType(){
        return "ImexportTask";
    }

}