package com.nancal.api.model;

import com.nancal.api.utils.FieldAlias;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.FidHistoryGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgOperationRevision 工序版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgOperationRevisionReq extends MfgOperationRevisionReq  implements Serializable{

    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String uid;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "对象名称")
    @NotBlank(message = "名称不能为空",groups = {AddGroup.class,UpdateGroup.class, FidHistoryGroup.class})
    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class, FidHistoryGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "名称",required = true,max = 128)
    private String objectName;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="工序类型",required=true)
    @NotBlank(message = "工序类型不能为空",groups = {AddGroup.class, FidHistoryGroup.class,UpdateGroup.class})
    @Length(max = 64,message = "长度超过了最大限制",groups = {AddGroup.class, FidHistoryGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工序-工序类型*",required = true,max = 64)
    @JsonDict(DictConstant.OP_TYPE)
    private String opType;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="是否关键工序",example="false")
//    @NotNull(message = "是否关键工序不能为空",groups = {AddGroup.class, FidHistoryGroup.class,UpdateGroup.class})
    private Boolean isKey;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="协作单位")
    @Length(max = 128,message = "长度超过了最大限制",groups = {AddGroup.class, FidHistoryGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工序-协作单位*",required = true,max = 128)
    @JsonDict(DictConstant.GTE4_COOPORG)
    private String gte4CoopOrg;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="代号ID",required=true)
    @Length(max = 64,message = "代号ID超过最大长度限制",groups = {LikeGroup.class})
    private String itemId;

    /**
     * 8-29，新加字段
     */
    @QueryField(Ops.LIKE)
//    @NotBlank(message = "工艺描述不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="工艺描述",required=true)
    @Length(max = 128,message = "工艺描述长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工艺描述",required = true,max = 128)
    private String gte4ActionDescription;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "检测要点不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="检测要点",required=true)
    @Length(max = 1024,message = "检测要点长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "检测要点",required = true,max = 1024)
    private String gte4ExaminePoints;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "工艺要求不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="工艺要求/作业要领",required=true)
    @Length(max = 255,message = "工艺要求长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工艺要求/作业要领",required = true,max = 255)
    private String gte4ProcessReq;

    @QueryField(Ops.EQ)
    @NotNull(message = "抽检比例不能为空")
    @ApiModelProperty(value="抽检比例",required=true)
    @FieldAlias(name = "抽检比例#",required = true)
    private Double gte4SamplingRatio;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "测试要点不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="测试要点",required=true)
    @Length(max = 255,message = "测试要点长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "测试要点",required = true,max = 255)
    private String gte4TestPoints;

    @QueryField(Ops.EQ)
//    @NotNull(message = "工时不能为空")
    @ApiModelProperty(value="工时(S)",required=true)
    @FieldAlias(name = "工序-工时(S)#",required = true)
    private Integer gte4TaskTime;

    @ApiModelProperty(value="备注",required=false)
    @Length(max = 512)
    @FieldAlias(name = "工序-备注",max = 512)
    private String remark;

    /**
     * 2022-11-07，新加字段
     */
    @ApiModelProperty(value="工艺能力组",required=false)
    @Length(max = 128,message = "工艺能力组超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工艺能力组",max = 128)
    private String gte4ActionCapabilityGroup;

    @ApiModelProperty(value="工艺能力组名称",required=false)
    @Length(max = 128,message = "工艺能力组名称长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工艺能力组名称",max = 128)
    private String gte4ActionCapabilityGroupName;

    @ApiModelProperty(value="板面",required=false)
    @Length(max = 128,message = "板面长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "板面",max = 128)
    private String gte4BoardSurface;




}