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

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgStep 工步 的请求")
@Data
@ToString
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgStepReq extends MfgStepReq  implements Serializable{

    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "id长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String uid;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "对象名称")
    @NotBlank(message = "对象名称不能为空",groups = {AddGroup.class, FidHistoryGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class, FidHistoryGroup.class,UpdateGroup.class,LikeGroup.class})
    @FieldAlias(name = "名称",required = true,max = 128)
    private String objectName;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="工步类型",required=true)
    @NotBlank(message = "工步类型不能为空",groups = {UpdateGroup.class, FidHistoryGroup.class,AddGroup.class})
    @Length(max = 64,message = "工步类型长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, FidHistoryGroup.class,LikeGroup.class})
    @FieldAlias(name = "工步-工序类型*",required = true,max = 64)
    @JsonDict(DictConstant.GTE4_TYPE)
    private String gte4Type;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="协作单位",required=true)
    @Length(max = 128,message = "协作单位长度超过了最大限制",groups = {AddGroup.class, FidHistoryGroup.class, UpdateGroup.class,LikeGroup.class})
    @FieldAlias(name = "工步-协作单位*",required = true,max = 128)
    @JsonDict(DictConstant.GTE4_COOPORG)
    private String gte4CoopOrg;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="代号ID(编号)")
    @Length(max = 64,message = "代号ID超过最大长度限制",groups = {LikeGroup.class})
    private String itemId;

    /**
     * 8-29，新加字段
     */
    @QueryField(Ops.EQ)
//    @NotNull(message = "工时不能为空")
    @ApiModelProperty(value="工时(S)",required=true)
    @FieldAlias(name = "工步-工时(S)#",required = true)
    private Integer gte4TaskTime;

    @QueryField(Ops.EQ)
//    @NotNull(message = "频率不能为空")
    @ApiModelProperty(value="频率",required=true)
    @FieldAlias(name = "频率#",required = true)
    private Double gte4Frequency;

    @ApiModelProperty(value="备注",required=false)
    @Length(max = 512)
    @FieldAlias(name = "工步-备注",max = 512)
    private String remark;
    @ApiModelProperty(value="数量",required=false)
    private Double quantity;

//    @NotNull(message = "查找编号不能为空",groups = {UpdateGroup.class})
    @ApiModelProperty(value="查找编号")
    private Integer foundNo;



}