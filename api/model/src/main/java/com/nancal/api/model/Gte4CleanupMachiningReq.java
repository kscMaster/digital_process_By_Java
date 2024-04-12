package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;


@ApiModel(value = "Gte4CleanupMachining 补加工记录 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gte4CleanupMachiningReq extends FormReq  implements Serializable{

    @ApiModelProperty(value="图号",required=false)
    @Length(max = 64)
    private String gte4PartNo;

    @ApiModelProperty(value="批次号",required=false)
    @Length(max = 64)
    private String gte4PartBn;

    @ApiModelProperty(value="顺序号",required=false)
    @Length(max = 64)
    private String gte4Sn;

    @ApiModelProperty(value="操作单位",required=false)
    @Length(max = 128)
    private String gte4OpOrg;

    @ApiModelProperty(value="操作人",required=false)
    @Length(max = 128)
    private String gte4OpPerson;

    @ApiModelProperty(value="操作日期",required=false,example="1994-03-07")
    private LocalDate gte4OpDate;

    @ApiModelProperty(value="关联技术文件",required=false)
    @Length(max = 128)
    private String gte4TechFile;

    @ApiModelProperty(value="技术文件号",required=false)
    @Length(max = 64)
    private String gte4TechNo;

    @ApiModelProperty(value="文件类型",required=false)
    @Length(max = 64)
    private String gte4FileType;

    @ApiModelProperty(value="初始型号",required=false)
    @Length(max = 64)
    private String gte4InitModel;

    @ApiModelProperty(value="阶段标识",required=false)
    @Length(max = 64)
    private String gte4Phase;

    @ApiModelProperty(value="批次",required=false)
    @Length(max = 64)
    private String gte4Bn;

    @ApiModelProperty(value="设计者",required=false)
    @Length(max = 128)
    private String gte4Designer;
}