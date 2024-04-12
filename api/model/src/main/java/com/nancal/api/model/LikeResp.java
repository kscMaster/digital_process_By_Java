package com.nancal.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.common.constants.DictConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName LikeResp
 * @Description TODO
 * @Author 拓凯
 * @Date 2022/4/13
 * @Version 1.0
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "LikeResp 模糊查询结果的响应")
public class LikeResp  extends WorkspaceObjectResp {

    @ApiModelProperty(value = "版本号")
    private String revision;

    @ApiModelProperty(value = "版次")
    private String sequence;

    @ApiModelProperty(value = "代(图)号")
    private String itemId;

    @ApiModelProperty(value = "主制单位")
    private String masterMakeUnit = "x001";

    @JsonDict(DictConstant.PHASE)
    @ApiModelProperty(value = "阶段")
    private String r006Phase;

    @JsonDict(DictConstant.IMP_KEY)
    @ApiModelProperty(value = "关重件")
    private String r006Key;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "生命周期状态更新时间", example = "1994-03-07")
    private Date changeDate;

    @ApiModelProperty("true已删除，false未删除")
    protected Boolean delFlag = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "最近修改时间", example = "1994-03-07")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "生命周期状态更新时间", example = "1994-03-07")
    private LocalDateTime stateChangeDate;

    @ApiModelProperty(name = "工艺路线")
    private String r006Route;

    @ApiModelProperty(value = "材料密度")
    private String r006MaterialDensity;

    @ApiModelProperty(name = "标准号")
    private String r006StandardNo;

    @JsonDict(DictConstant.PART_TYPE)
    @ApiModelProperty(name = "零件类型")
    private String r006ItemType;

    @JsonDict(DictConstant.WORKSHOP_TYPE)
    @ApiModelProperty(name = "发放单位")
    private String r006Unit;

    @JsonDict(DictConstant.WORKSHOP_TYPE)
    @ApiModelProperty(name = "主制单位")
    private String r006ManufacturingUnit;
}
